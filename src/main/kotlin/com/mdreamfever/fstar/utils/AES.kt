package com.mdreamfever.fstar.utils

import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesCryptUtil {
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private const val CHARSET = "UTF-8"
    private const val CIPHER = "AES"
    private const val HASH_ALGORITHM = "SHA-256"

    private val IV_BYTES = "**4W@#sI7jmTTn@d".toByteArray()
    private const val KEY = "V21CasMa4Cv7ij^G"

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val bytes = password.toByteArray(charset(CHARSET))
        digest.update(bytes, 0, bytes.size)
        return SecretKeySpec(password.toByteArray(), CIPHER)
    }


    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     * @param password used to generated key
     * @param message  the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     * @throws GeneralSecurityException if problems occur during encryption
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(password: String, message: String): String {
        try {
            val key = generateKey(password)
            val cipherText = encrypt(key, IV_BYTES, message.toByteArray(charset(CHARSET)))
            //NO_WRAP is important as was getting \n at the end
            return Base64.getEncoder().encodeToString(cipherText)
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }
    }


    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(key: SecretKeySpec, iv: ByteArray, message: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        return cipher.doFinal(message)
    }


    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param password                used to generated key
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     * @throws GeneralSecurityException if there's an issue decrypting
     */
    @Throws(GeneralSecurityException::class)
    fun decrypt(password: String, base64EncodedCipherText: String): String {

        try {
            val key = generateKey(password)
            val decodedCipherText = Base64.getDecoder().decode(base64EncodedCipherText)
            val decryptedBytes = decrypt(key, IV_BYTES, decodedCipherText)
            return String(decryptedBytes, charset(CHARSET))
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }

    }

    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    @Throws(GeneralSecurityException::class)
    fun decrypt(key: SecretKeySpec, iv: ByteArray, decodedCipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        return cipher.doFinal(decodedCipherText)
    }

    @Throws(GeneralSecurityException::class)
    fun decryptS(base64EncodedCipherText: String): String {
        try {
            val key = generateKey(KEY)
            val decodedCipherText = Base64.getDecoder().decode(base64EncodedCipherText)
            val decryptedBytes = decrypt(key, IV_BYTES, decodedCipherText)
            return String(decryptedBytes, charset(CHARSET))
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }
    }

    @Throws(GeneralSecurityException::class)
    fun encryptS(message: String): String {
        try {
            val key = generateKey(KEY)
            val cipherText = encrypt(key, IV_BYTES, message.toByteArray(charset(CHARSET)))
            //NO_WRAP is important as was getting \n at the end
            return Base64.getEncoder().encodeToString(cipherText)
        } catch (e: UnsupportedEncodingException) {
            throw GeneralSecurityException(e)
        }
    }
}