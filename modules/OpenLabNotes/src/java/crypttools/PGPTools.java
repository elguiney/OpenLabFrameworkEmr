package crypttools;

import java.io.File;


import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator; 
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;
 
/**
 * 
 * Copyright George El-Haddad</br>
 * <b>Time stamp:</b> Dec 6, 2012 - 11:41:43 AM<br/>
 * @author George El-Haddad
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
 *
 */
public final class PGPTools {
 
    public static final int PRIME_MODULUS_4096_BIT = 1;
    public static final int PRIME_MODULUS_3072_BIT = 2;
    public static final int PRIME_MODULUS_2048_BIT = 3;
    public static final int PRIME_MODULUS_1536_BIT = 4;
     
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
 
    private PGPTools() {
 
    }
 
    /**
     * 
     * @param dsaKeyPair - the generated DSA key pair
     * @param elGamalKeyPair - the generated El Gamal key pair
     * @param identity - the given identity of the key pair ring
     * @param passphrase - the secret pass phrase to protect the key pair
     * @return a PGP Key Ring Generate with the El Gamal key pair added as sub key
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static final PGPKeyRingGenerator createPGPKeyRingGenerator(KeyPair dsaKeyPair, KeyPair elGamalKeyPair, String identity, char[] passphrase) throws Exception
    {
        PGPKeyPair dsaPgpKeyPair = new PGPKeyPair(PGPPublicKey.DSA, dsaKeyPair, new Date());
        PGPKeyPair elGamalPgpKeyPair = new PGPKeyPair(PGPPublicKey.ELGAMAL_ENCRYPT, elGamalKeyPair, new Date());
        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);

        PGPContentSignerBuilder pgpCSB = new JcaPGPContentSignerBuilder(dsaPgpKeyPair.getPublicKey().getAlgorithm(),HashAlgorithmTags.SHA1);
        PBESecretKeyEncryptor pbeSKE = new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1Calc).setProvider("BC").build(passphrase);
        
        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, dsaPgpKeyPair, identity, sha1Calc, null, null, pgpCSB, pbeSKE);
         
        keyRingGen.addSubKey(elGamalPgpKeyPair);
        return keyRingGen;
    }
 
    /**
     * 
     * @param keySize 512 - 1024 (multiple of 64)
     * @return the DSA generated key pair
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     */
    public static final KeyPair generateDsaKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "BC");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
 
    /**
     * 
     * @param keySize - 1024, 2048, 4096
     * @return the El Gamal generated key pair
     * @throws Exception 
     */
    public static final KeyPair generateElGamalKeyPair(int keySize) throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ELGAMAL", "BC");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
 
    /**
     * 
     * @param paramSpecs - the pre-defined parameter specs
     * @return the El Gamal generated key pair
     * @throws Exception
     */
    public static final KeyPair generateElGamalKeyPair(ElGamalParameterSpec paramSpecs) throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ELGAMAL", "BC");
        keyPairGenerator.initialize(paramSpecs);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
     
    /**
     * <p>Given the size in bits
     * <ul>
     *  <li>PRIME_MODULUS_4096_BIT</li>
     *  <li>PRIME_MODULUS_3072_BIT</li>
     *  <li>PRIME_MODULUS_2048_BIT</li>
     *  <li>PRIME_MODULUS_1536_BIT</li>
     * </ul>
     * 
     * It will return a safe prime modulus {@link BigInteger}</p>
     * 
     * @param bitSize - the size in bits
     * @return a safe prime modulus of the specified size of <code>bitSize</code>
     */
    public static final BigInteger getSafePrimeModulus(int bitSize)
    {
        switch(bitSize)
        {
            case PRIME_MODULUS_4096_BIT:
            {
                return getSafePrimeModulus4096();
            }
             
            case PRIME_MODULUS_3072_BIT:
            {
                return getSafePrimeModulus3072();
            }
             
            case PRIME_MODULUS_2048_BIT:
            {
                return getSafePrimeModulus2048();
            }
             
            case PRIME_MODULUS_1536_BIT:
            {
                return getSafePrimeModulus1536();
            }
             
            default:
            {
                return getSafePrimeModulus1536();
            }
        }
    }
     
    /**
     * This is a 4096 bit MODP Group 
     * Prime number is: 2^4096 - 2^4032 - 1 + 2^64 * { [2^3996 pi] + 240904 }
     * 
     * @return a 4096 bit MODP group safe prime modulus
     */
    public static final BigInteger getSafePrimeModulus4096()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1");
        sb.append("29024E088A67CC74020BBEA63B139B22514A08798E3404DD");
        sb.append("EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245");
        sb.append("E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED");
        sb.append("EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D");
        sb.append("C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F");
        sb.append("83655D23DCA3AD961C62F356208552BB9ED529077096966D");
        sb.append("670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B");
        sb.append("E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9");
        sb.append("DE2BCBF6955817183995497CEA956AE515D2261898FA0510");
        sb.append("15728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64");
        sb.append("ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7");
        sb.append("ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6B");
        sb.append("F12FFA06D98A0864D87602733EC86A64521F2B18177B200C");
        sb.append("BBE117577A615D6C770988C0BAD946E208E24FA074E5AB31");
        sb.append("43DB5BFCE0FD108E4B82D120A92108011A723C12A787E6D7");
        sb.append("88719A10BDBA5B2699C327186AF4E23C1A946834B6150BDA");
        sb.append("2583E9CA2AD44CE8DBBBC2DB04DE8EF92E8EFC141FBECAA6");
        sb.append("287C59474E6BC05D99B2964FA090C3A2233BA186515BE7ED");
        sb.append("1F612970CEE2D7AFB81BDD762170481CD0069127D5B05AA9");
        sb.append("93B4EA988D8FDDC186FFB7DC90A6C08F4DF435C934063199");
        sb.append("FFFFFFFFFFFFFFFF");
        return new BigInteger(sb.toString(), 16);
    }
     
    /**
     * This is a 3072 bit MODP Group
     * Prime is: 2^3072 - 2^3008 - 1 + 2^64 * { [2^2942 pi] + 1690314 }
     * 
     * @return a 3072 bit MODP group safe prime modulus
     */
    public static final BigInteger getSafePrimeModulus3072()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1");
        sb.append("29024E088A67CC74020BBEA63B139B22514A08798E3404DD");
        sb.append("EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245");
        sb.append("E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED");
        sb.append("EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D");
        sb.append("C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F");
        sb.append("83655D23DCA3AD961C62F356208552BB9ED529077096966D");
        sb.append("670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B");
        sb.append("E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9");
        sb.append("DE2BCBF6955817183995497CEA956AE515D2261898FA0510");
        sb.append("15728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64");
        sb.append("ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7");
        sb.append("ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6B");
        sb.append("F12FFA06D98A0864D87602733EC86A64521F2B18177B200C");
        sb.append("BBE117577A615D6C770988C0BAD946E208E24FA074E5AB31");
        sb.append("43DB5BFCE0FD108E4B82D120A93AD2CAFFFFFFFFFFFFFFFF");
        return new BigInteger(sb.toString(), 16);
    }
     
    /**
     * This is a 2048 bit MODP Group
     * Prime is: 2^2048 - 2^1984 - 1 + 2^64 * { [2^1918 pi] + 124476 }
     * 
     * @return a 2048 bit MODP group safe prime modulus
     */
    public static final BigInteger getSafePrimeModulus2048()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1");
        sb.append("29024E088A67CC74020BBEA63B139B22514A08798E3404DD");
        sb.append("EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245");
        sb.append("E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED");
        sb.append("EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D");
        sb.append("C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F");
        sb.append("83655D23DCA3AD961C62F356208552BB9ED529077096966D");
        sb.append("670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B");
        sb.append("E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9");
        sb.append("DE2BCBF6955817183995497CEA956AE515D2261898FA0510");
        sb.append("15728E5A8AACAA68FFFFFFFFFFFFFFFF");
        return new BigInteger(sb.toString(), 16);
    }
     
    /**
     * This is a 1536 bit MODP Group
     * Prime is: 2^1536 - 2^1472 - 1 + 2^64 * { [2^1406 pi] + 741804 }
     * 
     * @return a 1536 bit MODP group safe prime modulus
     */
    public static final BigInteger getSafePrimeModulus1536()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1");
        sb.append("29024E088A67CC74020BBEA63B139B22514A08798E3404DD");
        sb.append("EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245");
        sb.append("E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED");
        sb.append("EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D");
        sb.append("C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F");
        sb.append("83655D23DCA3AD961C62F356208552BB9ED529077096966D");
        sb.append("670C354E4ABC9804F1746C08CA237327FFFFFFFFFFFFFFFF");
        return new BigInteger(sb.toString(), 16);
    }
     
    /**
     * 
     * @return the base generator number
     */
    public static final BigInteger getBaseGenerator()
    {
        return new BigInteger("2", 16);
    }
}