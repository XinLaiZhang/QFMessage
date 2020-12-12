import jsencrypt from 'jsencrypt'

var crypt = new jsencrypt();
export default{
	encode(publickey, str){
		crypt.setPublicKey('-----BEGIN PUBLIC KEY-----' + publickey + '-----END PUBLIC KEY-----');
		return crypt.encrypt(str);
	},
	decode(privatekey, str){
		crypt.setPrivateKey('-----BEGIN RSA PRIVATE KEY-----' + privatekey + '-----END RSA PRIVATE KEY-----');
		return crypt.decrypt(encrypted);
	}
}
