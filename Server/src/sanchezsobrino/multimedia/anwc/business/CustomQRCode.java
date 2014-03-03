package sanchezsobrino.multimedia.anwc.business;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.glxn.qrgen.QRCode;

public class CustomQRCode {
	private final Color red_color = new Color(0xffdd0000);
	private final Color green_color = new Color(0xff009900);
	
	private BufferedImage raw_qr_code;
	private ImageIcon red_qr_code;
	private ImageIcon green_qr_code;
	
	public CustomQRCode(String str, int width, int height) throws IOException {
		raw_qr_code = ImageIO.read(QRCode.from(str).withSize(width, height).file());
		red_qr_code = new ImageIcon(replaceColor(raw_qr_code, Color.black, red_color));
		green_qr_code = new ImageIcon(replaceColor(raw_qr_code, Color.black, green_color));
	}
	
	public ImageIcon getRedQRCode() {
		return red_qr_code;
	}
	
	public ImageIcon getGreenQRCode() {
		return green_qr_code;
	}
	
	private BufferedImage replaceColor(BufferedImage image, Color src, Color dst) {
		BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = copy.createGraphics();
		g.drawRenderedImage(image, null);
		g.dispose();

		for (int i = 0; i < copy.getHeight(); i++) {
			for (int j = 0; j < copy.getWidth(); j++) {
				Color rgb = new Color(copy.getRGB(i, j));
				if (src.equals(rgb)) {
					copy.setRGB(i, j, dst.getRGB());
				} else {
					copy.setRGB(i, j, Color.TRANSLUCENT);
				}
			}
		}

		return copy;
	}
}