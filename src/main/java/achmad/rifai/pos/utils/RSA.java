package achmad.rifai.pos.utils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

public class RSA {
    private final File pri,pub;

    public RSA(File pri, File pub) throws GeneralSecurityException, IOException {
        this.pri = pri;
        this.pub = pub;
        if(!pri.exists()||!pub.exists())
            buatKunci();
    }

    public String encrypt(String s) throws GeneralSecurityException, IOException, ClassNotFoundException {
        Cipher c=Cipher.getInstance("RSA");
        PublicKey k=loadPub();
        c.init(Cipher.ENCRYPT_MODE,k);
        return Work.toStrBts(c.doFinal(s.getBytes()));
    }

    public String decrypt(String s) throws GeneralSecurityException, IOException, ClassNotFoundException {
        Cipher c=Cipher.getInstance("RSA");
        PrivateKey k=loadPri();
        c.init(Cipher.DECRYPT_MODE,k);
        return new String(c.doFinal(Work.toBtsStr(s)));
    }

    private PrivateKey loadPri() throws IOException, ClassNotFoundException {
        FileInputStream f=new FileInputStream(pri);
        ObjectInputStream i=new ObjectInputStream(f);
        PrivateKey k= (PrivateKey) i.readObject();
        i.close();
        f.close();
        return k;
    }

    private PublicKey loadPub() throws IOException, ClassNotFoundException {
        FileInputStream f=new FileInputStream(pub);
        ObjectInputStream i=new ObjectInputStream(f);
        PublicKey k= (PublicKey) i.readObject();
        i.close();
        f.close();
        return k;
    }

    private void buatKunci() throws GeneralSecurityException, IOException {
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2101);
        KeyPair kp=kpg.generateKeyPair();
        savePri(kp.getPrivate());
        savePub(kp.getPublic());
    }

    private void savePub(PublicKey k) throws IOException {
        if(!pub.getParentFile().exists())pub.getParentFile().mkdirs();
        if(pub.exists())pub.delete();
        FileOutputStream f=new FileOutputStream(pub);
        ObjectOutputStream o=new ObjectOutputStream(f);
        o.writeObject(k);
        o.close();
        f.close();
    }

    private void savePri(PrivateKey k) throws IOException {
        if(!pri.getParentFile().exists())pri.getParentFile().mkdirs();
        if(pri.exists())pri.delete();
        FileOutputStream f=new FileOutputStream(pri);
        ObjectOutputStream o=new ObjectOutputStream(f);
        o.writeObject(k);
        o.close();
        f.close();
    }
}
