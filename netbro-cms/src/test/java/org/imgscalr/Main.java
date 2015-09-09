package org.imgscalr;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public class Main {
	// static ConvolveOp OP_ANTIALIAS = new ConvolveOp(
	// new Kernel(2, 2, new float[] { .125f, .375f, .375f, .125f }),
	// ConvolveOp.EDGE_NO_OP, null);

	static ConvolveOp OP_ANTIALIAS = new ConvolveOp(new Kernel(2, 2,
			new float[] { .25f, .25f, .25f, .25f }), ConvolveOp.EDGE_NO_OP,
			null);

	public static void main(String[] args) throws IOException {
		BufferedImage i = ImageIO.read(Main.class
				.getResourceAsStream("imgscalr-mac.png"));

		System.setProperty(Scalr.DEBUG_PROPERTY_NAME, "true");
		
		ImageIO.write(Scalr.resize(i, Mode.FIT_EXACT, 500, 250),
				"PNG", new FileOutputStream("imgscalr-mac-fit-exact-500x250.png"));
		
		ImageIO.write(Scalr.resize(i, Method.QUALITY, 136, Scalr.OP_ANTIALIAS),
				"PNG", new FileOutputStream("imgscalr-mac-quality-aa.png"));
		
		ImageIO.write(Scalr.resize(i, Method.QUALITY, 136),
				"PNG", new FileOutputStream("imgscalr-mac-quality.png"));

		ImageIO.write(Scalr.resize(Scalr.apply(i, OP_ANTIALIAS),
				Method.QUALITY, 136), "PNG", new FileOutputStream(
				"imgscalr-mac-quality-pre-aa-magic.png"));

		ImageIO.write(Scalr.resize(i, Method.QUALITY, 136, OP_ANTIALIAS),
				"PNG",
				new FileOutputStream("imgscalr-mac-quality-aa-magic.png"));
	}
}