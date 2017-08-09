package com.trs.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class CMySign
{
  private static final Logger LOG = Logger.getLogger(CMySign.class);

  private static final Map INSTANCES = new HashMap(4);
  private Signature m_signat;
  private Signature m_verify;
  private static final char[] encode_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };

  private static final byte[] decode_array = new byte[256];

  public byte[] decodeRadix64(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length / 4 * 3;
    if (i == 0) {
      return paramArrayOfByte;
    }
    if (paramArrayOfByte[(paramArrayOfByte.length - 1)] == 46) {
      i--;
      if (paramArrayOfByte[(paramArrayOfByte.length - 2)] == 46)
        i--;
    }
    byte[] arrayOfByte = new byte[i];

    int j = 0; int k = 0;
    i = paramArrayOfByte.length;
    while (i > 0)
    {
      int m = decode_array[(paramArrayOfByte[(j++)] & 0xFF)];
      int n = decode_array[(paramArrayOfByte[(j++)] & 0xFF)];

      arrayOfByte[(k++)] = (byte)(m << 2 & 0xFC | n >>> 4 & 0x3);

      if (paramArrayOfByte[j] == 46)
        return arrayOfByte;
      m = n;
      n = decode_array[(paramArrayOfByte[(j++)] & 0xFF)];

      arrayOfByte[(k++)] = (byte)(m << 4 & 0xF0 | n >>> 2 & 0xF);

      if (paramArrayOfByte[j] == 46)
        return arrayOfByte;
      m = n;
      n = decode_array[(paramArrayOfByte[(j++)] & 0xFF)];

      arrayOfByte[(k++)] = (byte)(m << 6 & 0xC0 | n & 0x3F);
      i -= 4;
    }
    return arrayOfByte;
  }

  public String encodeRadix64(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length == 0) {
      return "";
    }
    byte[] arrayOfByte = new byte[(paramArrayOfByte.length + 2) / 3 * 4];
    int i = 0; int j = 0;
    int k = paramArrayOfByte.length;
    while (k > 0)
    {
      int m;
      int n;
      int i1;
      if (k == 1) {
        m = paramArrayOfByte[(i++)];
        n = 0;
        i1 = 0;
        arrayOfByte[(j++)] = (byte)encode_array[(m >>> 2 & 0x3F)];
        arrayOfByte[(j++)] = (byte)encode_array[((m << 4 & 0x30) + (n >>> 4 & 0xF))];

        arrayOfByte[(j++)] = 46;
        arrayOfByte[(j++)] = 46;
      } else if (k == 2) {
        m = paramArrayOfByte[(i++)];
        n = paramArrayOfByte[(i++)];
        i1 = 0;
        arrayOfByte[(j++)] = (byte)encode_array[(m >>> 2 & 0x3F)];
        arrayOfByte[(j++)] = (byte)encode_array[((m << 4 & 0x30) + (n >>> 4 & 0xF))];

        arrayOfByte[(j++)] = (byte)encode_array[((n << 2 & 0x3C) + (i1 >>> 6 & 0x3))];

        arrayOfByte[(j++)] = 46;
      } else {
        m = paramArrayOfByte[(i++)];
        n = paramArrayOfByte[(i++)];
        i1 = paramArrayOfByte[(i++)];
        arrayOfByte[(j++)] = (byte)encode_array[(m >>> 2 & 0x3F)];
        arrayOfByte[(j++)] = (byte)encode_array[((m << 4 & 0x30) + (n >>> 4 & 0xF))];

        arrayOfByte[(j++)] = (byte)encode_array[((n << 2 & 0x3C) + (i1 >>> 6 & 0x3))];

        arrayOfByte[(j++)] = (byte)encode_array[(i1 & 0x3F)];
      }
      k -= 3;
    }
    return new String(arrayOfByte);
  }

  public static CMySign getSign(String paramString)
    throws Exception
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Debug Code", new Exception("For call stack inspect."));
      LOG.debug("Key File: " + paramString);
    }

    CMySign localCMySign = (CMySign)INSTANCES.get(paramString);
    if (localCMySign == null) {
      FileInputStream localFileInputStream = null;
      ByteArrayOutputStream localByteArrayOutputStream = null;
      try {
        localFileInputStream = new FileInputStream(paramString);
        localByteArrayOutputStream = new ByteArrayOutputStream(512);
        int i = 0;
        byte[] arrayOfByte = new byte[512];
        while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
        }

        arrayOfByte = localByteArrayOutputStream.toByteArray();
        PKCS8EncodedKeySpec localPKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(arrayOfByte);
        KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
        PrivateKey localPrivateKey = localKeyFactory.generatePrivate(localPKCS8EncodedKeySpec);

        Signature localSignature = Signature.getInstance("DSA");
        localSignature.initSign(localPrivateKey);

        localCMySign = new CMySign();
        localCMySign.m_signat = localSignature;
        INSTANCES.put(paramString, localCMySign);
      } finally {
        if (localFileInputStream != null)
          try {
            localFileInputStream.close();
          }
          catch (Exception localException1) {
          }
        if (localByteArrayOutputStream != null)
          try {
            localByteArrayOutputStream.close();
          }
          catch (Exception localException2)
          {
          }
      }
    }
    return localCMySign;
  }

  public static CMySign getVerify(String paramString)
    throws Exception
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Debug Code", new Exception("For call stack inspect."));
      LOG.debug("Key File: " + paramString);
    }
    CMySign localCMySign = (CMySign)INSTANCES.get(paramString);
    if (localCMySign == null) {
      FileInputStream localFileInputStream = null;
      ByteArrayOutputStream localByteArrayOutputStream = null;
      try {
        localFileInputStream = new FileInputStream(paramString);
        localByteArrayOutputStream = new ByteArrayOutputStream(512);
        int i = 0;
        byte[] arrayOfByte = new byte[512];
        while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
          localByteArrayOutputStream.write(arrayOfByte, 0, i);
        }

        arrayOfByte = localByteArrayOutputStream.toByteArray();

        X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(arrayOfByte);
        KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
        PublicKey localPublicKey = localKeyFactory.generatePublic(localX509EncodedKeySpec);
        Signature localSignature = Signature.getInstance("DSA");
        localSignature.initVerify(localPublicKey);

        localCMySign = new CMySign();
        localCMySign.m_verify = localSignature;
        INSTANCES.put(paramString, localCMySign);
      } finally {
        if (localFileInputStream != null)
          try {
            localFileInputStream.close();
          }
          catch (Exception localException1) {
          }
        if (localByteArrayOutputStream != null)
          try {
            localByteArrayOutputStream.close();
          }
          catch (Exception localException2)
          {
          }
      }
    }
    return localCMySign;
  }

  public byte[] sign(byte[] paramArrayOfByte)
    throws Exception
  {
    Signature localSignature = this.m_signat;

    synchronized (localSignature) {
      localSignature.update(paramArrayOfByte);
      return localSignature.sign();
    }
  }

  public boolean verify(byte[] paramArrayOfByte, int paramInt)
    throws Exception
  {
    int i = paramArrayOfByte.length - paramInt;
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, paramInt);
    Signature localSignature = this.m_verify;
    synchronized (localSignature) {
      localSignature.update(paramArrayOfByte, 0, i - 4);
      return localSignature.verify(arrayOfByte); } 
  }
  public static void main(String[] paramArrayOfString) { String str = "";
    ByteArrayOutputStream localByteArrayOutputStream = null;
    DataOutputStream localDataOutputStream = null;
    Object localObject1 = null;
    try { CMySign localCMySign = getSign("/f:/private_platform_log.key");

      localByteArrayOutputStream = new ByteArrayOutputStream(512);
      localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);

      localDataOutputStream.writeInt(1);
      localDataOutputStream.writeUTF("admin");
      localDataOutputStream.writeUTF("fuck&damn&fuck");
      localDataOutputStream.flush();

      localObject1 = localCMySign.sign(localByteArrayOutputStream.toByteArray());
      localDataOutputStream.writeInt(((byte[])localObject1).length);
      localDataOutputStream.write((byte[])localObject1);
      localDataOutputStream.flush();

      str = localCMySign.encodeRadix64(localByteArrayOutputStream.toByteArray());

      System.out.println("m_sDataWithSign:" + str);
    } catch (Exception localException1) {
      localException1.printStackTrace();
    } finally {
      if (localDataOutputStream != null)
        try {
          localDataOutputStream.close();
        } catch (Exception localException3) {
        }
      if (localByteArrayOutputStream != null) {
        try {
          localByteArrayOutputStream.close();
        }
        catch (Exception localException4)
        {
        }
      }
    }
    try
    {
      str = "AAAAAQAFYWRtaW4ADmZ1Y2smZGFtbiZmdWNrAAAALzAtAhUAhKx4_Y46slqXWrpAwo41o7R5v44CFFqciKKysJislreuU9hQWz72U1Mj";
      str = "AAAAAQAFYWRtaW4ADmZ1Y2smZGFtbiZmdWNrAAAALjAsAhQbtIMrFRVjZv1GzKrKwyw7Dfn0egIUGju0EhMQ3jm_ilfmknjb9o8Q_24.";
      byte[] arrayOfByte = str.getBytes();
      localObject1 = getVerify("/f:/public_platform_log.key");
      arrayOfByte = ((CMySign)localObject1).decodeRadix64(arrayOfByte);
      DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte));

      System.out.println(localDataInputStream.readInt());
      System.out.println(localDataInputStream.readUTF());
      System.out.println(localDataInputStream.readUTF());
      int i = localDataInputStream.readInt();
      System.out.println(i);
      if (!((CMySign)localObject1).verify(arrayOfByte, i))
        System.out.println("sign error");
      else {
        System.out.println("Verify true");
      }
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
    System.out.println(System.getProperty("file.encoding", "dd"));
    System.out.println(createEncodeUserInfo("/f:/private_platform_log.key", "zengrong&true&zengrong"));
  }

  public static String createEncodeUserInfo(String paramString1, String paramString2)
  {
    ByteArrayOutputStream localByteArrayOutputStream = null;
    DataOutputStream localDataOutputStream = null;
    String str3 = null;
    try {
      CMySign localCMySign = getSign(paramString1);

      localByteArrayOutputStream = new ByteArrayOutputStream(1024);
      localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);

      localDataOutputStream.writeInt(1);
      localDataOutputStream.writeUTF("admin");

      String str1 = CMyString.showEmpty(paramString2);
      localDataOutputStream.writeUTF(str1);
      localDataOutputStream.flush();

      byte[] arrayOfByte = localCMySign.sign(localByteArrayOutputStream.toByteArray());
      localDataOutputStream.writeInt(arrayOfByte.length);
      localDataOutputStream.write(arrayOfByte);
      localDataOutputStream.flush();

      String str2 = localCMySign.encodeRadix64(localByteArrayOutputStream.toByteArray());
      str3 = str2;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    } finally {
      if (localDataOutputStream != null)
        try {
          localDataOutputStream.close();
        } catch (Exception localException2) {
        }
      if (localByteArrayOutputStream != null)
        try {
          localByteArrayOutputStream.close();
        } catch (Exception localException3) {
        }
    }
    return "";
  }

  static
  {
    for (int i = 0; i < 255; i++) {
      decode_array[i] = -1;
    }
    for (int i = 0; i < encode_array.length; i++)
      decode_array[encode_array[i]] = (byte)i;
  }
}