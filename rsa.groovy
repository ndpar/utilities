#!/usr/bin/env groovy

/**
 * Extract RSA private key in PKCS#1 PEM format from a keystore.
 * This functionality is missing in the standard `keytool`.
 *
 * This script is a toy example to demonstrate few techniques.
 * If you need to extract a private key from your PKCS#12 keystore,
 * use this command, which works for all algorithms:
 * 
 * $ openssl pkcs12 -in keystore.p12 -nocerts -nodes
 *
 * It outputs keys in PKCS#8 PEM format which is universal.
 *
 * If your kestore is in JKS format, convert it to PKCS#12 first:
 *
 * $ keytool -importkeystore -srckeystore keystore.jks -srcstoretype JKS -destkeystore keystore.p12 -deststoretype PKCS12
 *
 * @see https://blog.ndpar.com/2017/04/17/p1-p8/
 * @see https://blog.ndpar.com/2017/04/24/cryptography-tools/
 *
 * @author Andrey Paramonov
 */
import java.security.KeyStore
import java.security.PrivateKey
import groovy.cli.commons.CliBuilder

def cli = new CliBuilder(usage:'rsa.groovy [options]')
cli.alias(args:1, argName:'alias', required:true, 'alias name of the key to process')
cli.keypass(args:1, argName:'keypass', required:true, 'key password')
cli.keystore(args:1, argName:'keystore', required:true, 'keystore name')
cli.storepass(args:1, argName:'storepass', required:true, 'keystore password')
cli.storetype(args:1, argName:'storetype', required:true, 'keystore type')

def options = cli.parse(args)
if (!options) return

KeyStore ks = KeyStore.getInstance(options.storetype)
ks.load(new FileInputStream(options.keystore), options.storepass.toCharArray())
PrivateKey sk = ks.getKey(options.alias, options.keypass.toCharArray()) as PrivateKey
String key = new String(Base64.encoder.encode(sk.encoded)).replaceAll(/(?<=\G\S{64})/, '\n')
println "-----BEGIN RSA PRIVATE KEY-----\n$key\n-----END RSA PRIVATE KEY-----"
