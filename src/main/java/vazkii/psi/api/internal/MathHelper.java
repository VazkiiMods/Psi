/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [23/01/2016, 00:09:20 (GMT)]
 */
package vazkii.psi.api.internal;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.*;

public final class MathHelper {

	public static float pointDistancePlane(double x1, double y1, double x2, double y2) {
		return (float) Math.hypot(x1 - x2, y1 - y2);
	}

	public static double pointDistanceSpace(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
	}
	public static LinkedHashSet<BlockPos> getBlocksAlongRay(Vec3d origin, Vec3d end) {
		return getBlocksAlongRay(origin, end, Integer.MAX_VALUE);
	}

	/**
	 * [VanillaCopy] of {@link net.minecraft.world.IWorldReader#func_217300_a(RayTraceContext, BiFunction, Function)}
	 * but without the extra processing and endpoint bumping
	 */
	public static LinkedHashSet<BlockPos> getBlocksAlongRay(Vec3d origin, Vec3d end, int maxBlocks) {
		LinkedHashSet<BlockPos> positions = new LinkedHashSet<>();
		if (origin.equals(end)) {
			positions.add(new BlockPos(origin.x, origin.y, origin.z));
		} else {
			double endX = end.x;
			double endY = end.y;
			double endZ = end.z;
			double originX = origin.x;
			double originY = origin.y;
			double originZ = origin.z;
			int blockX = net.minecraft.util.math.MathHelper.floor(originX);
			int blockY = net.minecraft.util.math.MathHelper.floor(originY);
			int blockZ = net.minecraft.util.math.MathHelper.floor(originZ);
			BlockPos.Mutable blockPos = new BlockPos.Mutable(blockX, blockY, blockZ);
			positions.add(blockPos.toImmutable());
			double lengthX = endX - originX;
			double lengthY = endY - originY;
			double lengthZ = endZ - originZ;
			int signumX = net.minecraft.util.math.MathHelper.signum(lengthX);
			int signumY = net.minecraft.util.math.MathHelper.signum(lengthY);
			int signumZ = net.minecraft.util.math.MathHelper.signum(lengthZ);
			double stepSizeX = signumX == 0 ? Double.MAX_VALUE : (double)signumX / lengthX;
			double stepSizeY = signumY == 0 ? Double.MAX_VALUE : (double)signumY / lengthY;
			double stepSizeZ = signumZ == 0 ? Double.MAX_VALUE : (double)signumZ / lengthZ;
			double totalStepsX = stepSizeX * (signumX > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originX) : net.minecraft.util.math.MathHelper.frac(originX));
			double totalStepsY = stepSizeY * (signumY > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originY) : net.minecraft.util.math.MathHelper.frac(originY));
			double totalStepsZ = stepSizeZ * (signumZ > 0 ? 1.0D - net.minecraft.util.math.MathHelper.frac(originZ) : net.minecraft.util.math.MathHelper.frac(originZ));

			while((totalStepsX <= 1.0D || totalStepsY <= 1.0D || totalStepsZ <= 1.0D) && positions.size() != maxBlocks) {
				if (totalStepsX < totalStepsY) {
					if (totalStepsX < totalStepsZ) {
						blockX += signumX;
						totalStepsX += stepSizeX;
					} else {
						blockZ += signumZ;
						totalStepsZ += stepSizeZ;
					}
				} else if (totalStepsY < totalStepsZ) {
					blockY += signumY;
					totalStepsY += stepSizeY;
				} else {
					blockZ += signumZ;
					totalStepsZ += stepSizeZ;
				}
				blockPos.setPos(blockX, blockY, blockZ);
				positions.add(blockPos.toImmutable());
			}
		}
		return positions;
	}


	/*
	 * https://github.com/massiccio/java/blob/master/src/math/GammaFunction.java
	 * Copyright (C) 2013 Michele Mazzucco
	 *
	 * This program is free software: you can redistribute it and/or modify
	 * it under the terms of the GNU General Public License as published by
	 * the Free Software Foundation, either version 3 of the License, or
	 * (at your option) any later version.
	 *
	 * This program is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 * GNU General Public License for more details.
	 *
	 * You should have received a copy of the GNU General Public License
	 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
	 */
	public static class GammaFunction {
		/** Maximum allowed number of iterations. */
		private static final int ITMAX = 100;

		/** Relative accuracy. */
		private static final double EPS = 3.0e-7;

		/** Number near the smallest representable. */
		private static final double FPMIN = Double.MIN_VALUE;

		private GammaFunction() {
			//
		}

		/**
		 * Computes the value of ln(gamma(xx)) for xx >0.
		 */
		public static double gammln(double xx) {
			double x, y, tmp, ser;
			x = y = tmp = ser = 0.0;

			final double[] cof = { 76.18009172947146, -86.50532032941677,
					24.01409824083091, -1.231739572450155, 0.1208650973866179e-2,
					-0.5395239384953e-5 };
			int j;
			y = x = xx;
			tmp = x + 5.5;
			tmp -= (x + 0.5) * log(tmp);
			ser = 1.000000000190015;
			for (j = 0; j <= 5; j++) {
				ser += cof[j] / ++y;
			}

			return -tmp + log(2.5066282746310005 * ser / x);
		}

		/**
		 * Computes &Gamma;(xx), where &Gamma; is the complete Gamma() function.
		 *
		 * @param xx A number.
		 * @return &Gamma;(xx).
		 */
		public static double gamma(double xx) {
			return exp(gammln(xx));
		}

		/**
		 * Computes the lower incomplete gamma function, &gamma;(a,x)= integral from
		 * zero to x of (exp(-t)t^(a-1))dt.
		 * <p>
		 * This is the same as Igamma(a, x, lower=TRUE) in the zipfR in R.
		 *
		 * @param a The parameter of the integral
		 * @param x The upper bound for the interval of integration
		 * @return The lower incomplete gamma function, &gamma;(s,x).
		 */
		public static double lowerIncomplete(double a, double x) {
			return regularizedGammaP(a, x) * gamma(a);
		}

		/**
		 * Computes the upper incomplete gamma function, &gamma;(s,x)= integral from
		 * x to infinity of (exp(-t)t^(s-1))dt.
		 * <p>
		 * This is the same as Igamma(a, x, lower=FALSE) in the zipfR in R.
		 *
		 * @param a The parameter of the integral
		 * @param x The lower bound for the interval of integration
		 * @return The upper incomplete gamma function, &gamma;(s,x).
		 */
		public static double upperIncomplete(double a, double x) {
			return regularizedGammaQ(a, x) * gamma(a);
		}

		/**
		 * Returns the upper incomplete <strong>regularised</strong> gamma function
		 * Q(a, x) = 1 − P(a, x).
		 * <p>
		 * This is the same as Rgamma(a, x, lower=FALSE) in the zipfR in R.
		 *
		 * @see <a
		 *      href="http://finzi.psych.upenn.edu/R/library/zipfR/html/beta_gamma.html">Incomplete
		 *      Beta and Gamma Functions (zipfR)</a>
		 */
		public static double regularizedGammaQ(double a, double x) {
			if (a <= 0.0)
				throw new IllegalArgumentException(
						"Invalid arguments in routine gammq");
			return 1.0 - regularizedGammaP(a, x);
		}

		/**
		 * Returns the lower incomplete <strong>regularised</strong> gamma function
		 * P(a, x)= &gamma;(a,x)/&Gamma;(a).
		 * <p>
		 * P(a, x) is the cumulative distribution function for Gamma random
		 * variables with shape parameter <i>a</i> and scale parameter 1.
		 * <p>
		 * This is the same as Rgamma(a, x, lower=TRUE) in the zipfR in R.
		 *
		 * @see <a
		 *      href="http://finzi.psych.upenn.edu/R/library/zipfR/html/beta_gamma.html">Incomplete
		 *      Beta and Gamma Functions (zipfR)</a>
		 * @see <a
		 *      href="http://en.wikipedia.org/wiki/Incomplete_gamma_function#Regularized_Gamma_functions_and_Poisson_random_variables">Regularized
		 *      Gamma functions and Poisson random variables</a>
		 */
		public static double regularizedGammaP(double a, double x) {
			if (a <= 0.0)
				throw new IllegalArgumentException(
						"Invalid arguments in routine gammp");
			if (x < (a + 1.0)) { // Use the series representation.
				double gamser = gser(a, x);
				return gamser;
			} else { // Use the continued fraction representation
				double gammcf = gcf(a, x);
				return 1.0 - gammcf; // and take its complement.
			}
		}

		/**
		 * Returns the incomplete gamma function P(a, x) evaluated by its series
		 * representation.
		 */
		private static double gser(double a, double x) {
			// Returns the incomplete gamma function P(a, x) evaluated by its series
			// representation as gamser.
			// Also returns ln gamma(a) as gln.
			// double sum, del, ap;
			double gln = gammln(a);
			if (x <= 0.0) {
				if (x < 0.0)
					throw new IllegalArgumentException("x < 0 in routine gser");
				return 0.0;
			} else {
				double gamser = 0.0;
				double ap = a;
				double del, sum;
				del = sum = 1.0 / a;
				for (int n = 1; n <= ITMAX; n++) {
					++ap;
					del *= x / ap;
					sum += del;
					if (abs(del) < abs(sum) * EPS) {
						gamser = sum * exp(-x + a * log(x) - gln);
						return gamser;
					}
				}
				throw new IllegalArgumentException(
						"a too large, ITMAX too small in routine gser");
			}
		}

		/**
		 * Computes the incomplete gamma function Q(a, x) = 1 - P(a,x) = 1 -
		 * integral from zero to x of (exp(-t)t^(a-1))dt evaluated by its continued
		 * fraction representation.
		 */
		private static double gcf(double a, double x) {
			double an, del;
			int i;
			double b = x + 1.0 - a; // Set up for evaluating continued fraction
			// by modified Lentz’s method (§5.2) with b0 = 0.
			double c = 1.0 / FPMIN;
			double d = 1.0 / b;
			double h = d;
			for (i = 1; i <= ITMAX; i++) { // Iterate to convergence.
				an = -i * (i - a);
				b += 2.0;
				d = an * d + b;
				if (abs(d) < FPMIN)
					d = FPMIN;
				c = b + an / c;
				if (abs(c) < FPMIN)
					c = FPMIN;
				d = 1.0 / d;
				del = d * c;
				h *= del;
				if (abs(del - 1.0) < EPS)
					break;
			}
			if (i > ITMAX)
				throw new IllegalArgumentException(
						"a too large, ITMAX too small in gcf");
			// Put factors in front.
			double gln = gammln(a);
			double gammcf = exp(-x + a * log(x) - gln) * h;
			return gammcf; // imcomplete gamma function Q(a,x)
		}

		public static void main(String[] args) {
			System.out.println(gamma(2.98));
			double a = 2.98;
			double x = 1.3;

			// Documentation at
			// http://zipfr.r-forge.r-project.org/materials/zipfR_0.6-5.pdf
			// Sec. beta_gamma
			// > Igamma(a,x, lower=T)
			System.out.println("lower incomplete " + lowerIncomplete(a, x));
			// > Igamma(a,x, lower=F)
			System.out.println("upper incomplete " + upperIncomplete(a, x));

			// > Rgamma(a,x, lower=T)
			System.out.println("reg. lower " + regularizedGammaP(a, x));
			// > Rgamma(a,x, lower=F)
			System.out.println("reg. upper " + regularizedGammaQ(a, x));
		}
	}

}
