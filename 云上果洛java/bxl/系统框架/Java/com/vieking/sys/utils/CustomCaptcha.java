package com.vieking.sys.utils;

import java.awt.Color;

import org.jboss.seam.annotations.Name;

import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.gimpy.BasicGimpyEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

@Name("customCaptcha")
public class CustomCaptcha {

	public ImageCaptchaService getService() {
		BasicGimpyEngine customCaptcha = new BasicGimpyEngine();
		GimpyFactory factory = new GimpyFactory(new RandomWordGenerator(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ23456789"), new ComposedWordToImage(
				new RandomFontGenerator(new Integer(15), new Integer(15)),
				new UniColorBackgroundGenerator(new Integer(150), new Integer(
						30)), new RandomTextPaster(new Integer(4), new Integer(
						7), Color.BLACK)));
		GimpyFactory[] factories = { factory };
		customCaptcha.setFactories(factories);

		return new DefaultManageableImageCaptchaService(
				new FastHashMapCaptchaStore(), customCaptcha, 180, 120000,
				75000);
	}
}
