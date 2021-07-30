package com.example.btcqrscanner.ui.keys;

import com.example.btcqrscanner.Validator;
import com.example.btcqrscanner.ui.addresses.Address;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;


public class Key implements Comparable<Key>, Serializable {

    private static final long serialVersionUID = 1L;

    public String privateKey;

    public Address addressCompressed;
    public Address addressUncompressed;
    public Address addressBECH32;
    public Address addressP2SH;


    public Key(String privateKey, int type){
        NetworkParameters params = MainNetParams.get();
        ECKey key;

        if (type == Validator.PRIVATE_KEY_HEX){
            this.privateKey = privateKey;
            key = ECKey.fromPrivate(new BigInteger(privateKey, 16));
        } else if (type == Validator.PRIVATE_KEY_DEC){
            key = ECKey.fromPrivate(new BigInteger(privateKey, 10));
            this.privateKey = key.getPrivateKeyAsHex();
        } else {
            DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, privateKey);
            key = dumpedPrivateKey.getKey();
            this.privateKey = key.getPrivateKeyAsHex();
        }

        if (!key.isCompressed()){
            key = ECKey.fromPrivate(new BigInteger(key.getPrivateKeyAsHex(), 16));
        }

        Script script = ScriptBuilder.createP2WPKHOutputScript(key);
        script = ScriptBuilder.createP2SHOutputScript(script);
        byte[] scriptHash = ScriptPattern.extractHashFromP2SH(script);

        String addressP2SH = LegacyAddress.fromScriptHash(params, scriptHash).toString();
        String addressCompressed = LegacyAddress.fromKey(params, key).toString();
        String addressBECH32 = SegwitAddress.fromKey(params,key).toString();
        String addressUncompressed = LegacyAddress.fromKey(params, key.decompress()).toString();

        this.addressCompressed = new Address(addressCompressed);
        this.addressUncompressed = new Address(addressUncompressed);
        this.addressBECH32 = new Address(addressBECH32);
        this.addressP2SH = new Address(addressP2SH);
    }

    public Key(String privateKey){
        this(privateKey, Validator.validate(privateKey));
    }

    public boolean isChecked(){
        return addressBECH32.isChecked() && addressP2SH.isChecked()
                && addressCompressed.isChecked() && addressUncompressed.isChecked();
    }

    public String getPrivateKey(){
        return privateKey;
    }

    public Address getAddressCompressed(){
        return addressCompressed;
    }

    public Address getAddressUncompressed() {
        return addressUncompressed;
    }

    public Address getAddressBECH32() {
        return addressBECH32;
    }

    public Address getAddressP2SH() {
        return addressP2SH;
    }

    public ArrayList<String> getAddresses(){
        return new ArrayList<>(Arrays.asList(
                addressCompressed.getAddress(),
                addressUncompressed.getAddress(),
                addressBECH32.getAddress(),
                addressP2SH.getAddress()));
    }

    @Override
    public int compareTo(Key o) {
        return addressUncompressed.compareTo(o.addressUncompressed);
    }

    @Override
    public String toString() {
        return "Key{" +
                "privateKey='" + privateKey + '\'' +
                ", addressCompressed=" + addressCompressed +
                ", addressUncompressed=" + addressUncompressed +
                ", addressBECH32=" + addressBECH32 +
                ", addressP2SH=" + addressP2SH +
                '}';
    }

    public String toEmail(){
        String s = getPrivateKey() + "\n";
        s += addressCompressed.toEmail();
        s += addressUncompressed.toEmail();
        s += addressP2SH.toEmail();
        s += addressBECH32.toEmail();
        return s;
    }
}
