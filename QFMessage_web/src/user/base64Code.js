import {Base64} from 'js-base64';

function encode(str) {
	try{
		if (str != null && str != '') return Base64.encodeURI(str);
		else return str;
	}catch(e){
		window.location = "./user.html";
	}
}
function decode(str) {
	try{
		if (str != null && str != '') return Base64.decode(str);
		else return str;
	}catch(e){
		window.location = "./user.html";
	}
}

export {decode, encode}