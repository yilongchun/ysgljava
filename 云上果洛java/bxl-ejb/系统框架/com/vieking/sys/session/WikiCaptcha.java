package com.vieking.sys.session;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.captcha.Captcha;
import org.jboss.seam.captcha.CaptchaResponse;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@Name("org.jboss.seam.captcha.captcha")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
public class WikiCaptcha extends Captcha {

	/** 
* 
*/
	private static final long serialVersionUID = 4353904503753742537L;

	private Color backgroundColor = new Color(0xf5, 0xf5, 0xf5);
	private Font textFont = new Font("verdana", Font.PLAIN, 30);
	private int charsToPrint = 4;
	private int width = 100;
	private int height = 30;
	private int circlesToDraw = 4;
	private float horizMargin = 10.0f;
	private double rotationRange = 0.2;
	private String elegibleChars = "123456789";
	private char[] chars = elegibleChars.toCharArray();

	@Override
	@Create
	public void init() {
		super.init();
		StringBuffer finalString = new StringBuffer();
		for (int i = 0; i < charsToPrint; i++) {
			double randomValue = Math.random();
			int randomIndex = (int) Math
					.round(randomValue * (chars.length - 1));
			char characterToShow = chars[randomIndex];
			finalString.append(characterToShow);
		}
		setChallenge(finalString.toString());
		setCorrectResponse(finalString.toString());
	}

	@Override
	public BufferedImage renderChallenge() {

		// Background
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);

		// Some obfuscation circles
		for (int i = 0; i < circlesToDraw; i++) {
			int circleColor = 80 + (int) (Math.random() * 70);
			float circleLinewidth = 0.3f + (float) (Math.random());
			g.setColor(new Color(circleColor, circleColor, circleColor));
			g.setStroke(new BasicStroke(circleLinewidth));
			int circleRadius = (int) (Math.random() * height / 2.0);
			int circleX = (int) (Math.random() * width - circleRadius);
			int circleY = (int) (Math.random() * height - circleRadius);
			g.drawOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
		}

		// Text
		g.setFont(textFont);
		FontMetrics fontMetrics = g.getFontMetrics();
		int maxAdvance = fontMetrics.getMaxAdvance();
		int fontHeight = fontMetrics.getHeight();
		float spaceForLetters = -horizMargin * 2 + width;
		float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);

		char[] allChars = getChallenge().toCharArray();
		for (int i = 0; i < allChars.length; i++) {
			char charToPrint = allChars[i];
			int charWidth = fontMetrics.charWidth(charToPrint);
			int charDim = Math.max(maxAdvance, fontHeight);
			int halfCharDim = (charDim / 2);
			BufferedImage charImage = new BufferedImage(charDim, charDim,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D charGraphics = charImage.createGraphics();
			charGraphics.translate(halfCharDim, halfCharDim);
			double angle = (Math.random() - 0.5) * rotationRange;
			charGraphics.transform(AffineTransform.getRotateInstance(angle));
			charGraphics.translate(-halfCharDim, -halfCharDim);
			int charColor = 60 + (int) (Math.random() * 90);
			charGraphics.setColor(new Color(charColor, charColor, charColor));
			charGraphics.setFont(textFont);
			int charX = (int) (0.5 * charDim - 0.5 * charWidth);
			charGraphics.drawString("" + charToPrint, charX,
					((charDim - fontMetrics.getAscent()) / 2 + fontMetrics
							.getAscent()));
			float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
			int y = ((height - charDim) / 2);
			g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
			charGraphics.dispose();
		}
		g.dispose();
		return bufferedImage;
	}

	@CaptchaResponse(message = "验证码错误")
	public String getResponse() {
		return super.getResponse();
	}

}