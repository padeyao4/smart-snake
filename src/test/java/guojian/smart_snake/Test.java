package guojian.smart_snake;

import java.util.ResourceBundle;

public class Test {
	private static ResourceBundle bundle = ResourceBundle.getBundle("config");
	public static void main(String[] args) {
		String imageDir = bundle.getString("screenWidth");
		System.out.println(imageDir);
	}
}
