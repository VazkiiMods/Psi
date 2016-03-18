/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [12/01/2016, 00:23:41 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.common.network.Message;

public class TestMessage extends Message {

	public byte b;
	public short s;
	public int i;
	public long l;
	public float f;
	public double d;
	public boolean bo;
	public char c;
	public String st;
	public NBTTagCompound cmp;
	public ItemStack stack;
	public BlockPos pos;

	public transient int trInt = 4;

	public TestMessage() { }

	public TestMessage(byte b, short s, int i, long l, float f, double d, boolean bo, char c, String st, NBTTagCompound cmp, ItemStack stack, BlockPos pos, int trInt) {
		this.b = b;
		this.s = s;
		this.i = i;
		this.l = l;
		this.f = f;
		this.d = d;
		this.bo = bo;
		this.c = c;
		this.st = st;
		this.cmp = cmp;
		this.stack = stack;
		this.pos = pos;

		this.trInt = trInt;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		System.out.println("byte is " + b);
		System.out.println("short is " + s);
		System.out.println("int is " + i);
		System.out.println("long is " + l);
		System.out.println("float is " + f);
		System.out.println("double is " + d);
		System.out.println("boolean is " + bo);
		System.out.println("char is " + c);
		System.out.println("String is " + st);
		System.out.println("NBTTagCompount is " + cmp);
		System.out.println("ItemStack is " + stack);
		System.out.println("BlockPos is " + pos);
		System.out.println("trInt is " + trInt);
		return null;
	}

}
