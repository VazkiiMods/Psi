/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [14/01/2016, 23:27:25 (GMT)]
 */
package vazkii.psi.common.lib;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class LibObfuscation {

	// GuiIngame
	public static final String REMAINING_HIGHLIGHT_TICKS = "field_92017_k";

	// NetHandlerPlayServer
	public static final String CAPTURE_CURRENT_POSITION = "func_184342_d";


	public static void getDescriptor(final Class<?> type, StringBuilder buf) {
		if (type.isPrimitive()) {
			if (type == Boolean.TYPE)
				buf.append('Z');
			else if (type == Byte.TYPE)
				buf.append('B');
			else if (type == Character.TYPE)
				buf.append('C');
			else if (type == Double.TYPE)
				buf.append('D');
			else if (type == Float.TYPE)
				buf.append('F');
			else if (type == Integer.TYPE)
				buf.append('I');
			else if (type == Long.TYPE)
				buf.append('J');
			else if (type == Short.TYPE)
				buf.append('S');
			else if (type == Void.TYPE)
				buf.append('V');
		} else if (type.isArray())
			buf.append(type.getName());
		else
			buf.append('L').append(type.getName().replace('.', '/')).append(';');
	}

	public static String getMethodDescriptor(Class<?> returnType,
											 Class<?>... argumentTypes) {
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		for (Class argumentType : argumentTypes) {
			getDescriptor(argumentType, buf);
		}
		buf.append(')');
		getDescriptor(returnType, buf);
		return buf.toString();
	}

	public static String remapMethodName(String className, String methodName, @Nonnull Class<?> returnType, Class<?>... parameterTypes) {
		String internalClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(className.replace('.', '/'));
		String signature = FMLDeobfuscatingRemapper.INSTANCE.mapSignature(getMethodDescriptor(returnType, parameterTypes), false);
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(internalClassName, methodName, signature);
	}

	public static Method findMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nonnull Class<?> returnType, Class<?>... parameterTypes) {
		return ReflectionHelper.findMethod(clazz, remapMethodName(clazz.getName(), methodName, returnType, parameterTypes), methodName, parameterTypes);
	}

	public static <T> Object callMethod(@Nonnull Class<? extends T> clazz, T instance, @Nonnull String methodName, Class<?>[] parameterTypes, @Nonnull Class<?> returnType, Object... arguments) {
		Method method = findMethod(clazz, methodName, returnType, parameterTypes);

		try {
			return method.invoke(instance, arguments);
		} catch (IllegalAccessException | InvocationTargetException e) {
			FMLLog.log.error("Unable to access method {} on type {}", methodName, clazz.getName());
			throw new RuntimeException(e);
		}
	}
}
