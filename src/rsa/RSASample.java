package rsa;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

/**
 * RSA暗号での暗号化のJavaでの実装例
 * 秘密鍵で暗号化 → 公開鍵で復号、公開鍵で暗号化 → 秘密鍵で復号の2パターンを実装
 * @author TAKANAMI
 *
 */
public class RSASample {
	public static void main(String[] args) throws Exception {
		// 公開鍵・秘密鍵を生成する。
		KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
		kg.initialize(1024);
		KeyPair keyPair = kg.generateKeyPair();
		KeyFactory factoty = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicKeySpec = factoty.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
		RSAPrivateKeySpec privateKeySpec = factoty.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
		PublicKey publicKey = factoty.generatePublic(publicKeySpec);
		PrivateKey privateKey = factoty.generatePrivate(privateKeySpec);

		// 平文
		byte[] plain = new byte[] { 0x01, 0x02 };

		// 秘密鍵で暗号化して公開鍵で復号する。
		Cipher encrypterWithPrivateKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		encrypterWithPrivateKey.init(Cipher.ENCRYPT_MODE,privateKey);
		byte[] encryptedWithPrivateKey = encrypterWithPrivateKey.doFinal(plain);
		System.out.println("encryptedWithPrivateKey (" + encryptedWithPrivateKey.length + " bytes):");
		System.out.println(decodeHex(encryptedWithPrivateKey));

	    Cipher decrypterWithPublicKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    decrypterWithPublicKey.init(Cipher.DECRYPT_MODE, publicKey);
	    byte[] decryptedWithPrivateKey = decrypterWithPublicKey.doFinal(encryptedWithPrivateKey);
	    System.out.println("decryptedWithPrivateKey (" + decryptedWithPrivateKey.length + " bytes):");
	    System.out.println(decodeHex(decryptedWithPrivateKey));

	    // 公開鍵で暗号化して秘密鍵で復号する。
	    Cipher encrypterWithPublicKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    encrypterWithPublicKey.init(Cipher.ENCRYPT_MODE, publicKey);
	    byte[] encryptedWithPublicKey = encrypterWithPublicKey.doFinal(plain);
	    System.out.println("encryptedWithPublicKey (" + encryptedWithPublicKey.length + " bytes):");
	    System.out.println(decodeHex(encryptedWithPublicKey));

	    Cipher decrypterWithPriavteKey = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    decrypterWithPriavteKey.init(Cipher.DECRYPT_MODE, privateKey);
	    byte[] decryptedWithPriavteKey = decrypterWithPriavteKey.doFinal(encryptedWithPublicKey);
	    System.out.println("decryptedWithPriavteKey (" + decryptedWithPriavteKey.length + " bytes):");
	    System.out.println(decodeHex(decryptedWithPriavteKey));
	}

	/** byte配列を16進数表記に変換する。 */
	public static String decodeHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}