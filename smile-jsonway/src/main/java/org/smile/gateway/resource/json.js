var JWay=function(){};
var m = {
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
    },
    s = {
        'array': function (x) {
            var a = ['['], b, f, i, l = x.length, v;
            for (var i = 0; i < l; i += 1) {
                v = x[i];
                f = s[typeof v];
                if (f) {
                    v = f(v);
                    if (typeof v == 'string') {
                        if (b) {
                            a[a.length] = ',';
                        }
                        a[a.length] = v;
                        b = true;
                    }
                }
            }
            a[a.length] = ']';
            return a.join('');
        },
        'boolean': function (x) {
            return String(x);
        },
        'null': function (x) {
            return "null";
        },
        'number': function (x) {
            return isFinite(x) ? String(x) : 'null';
        },
        'object': function (x) {           	
            if (x) {					              	
                if (x instanceof Array||x.length) {
                    return s.array(x);
                }else if(x instanceof Object){
                	var a = ['{'], b, f, i, v;
                    for (var i in x) {
                        v = x[i];
                        f = s[typeof v];
                        if (f) {
                            v = f(v);
                            if (typeof v == 'string') {
                                if (b) {
                                    a[a.length] = ',';
                                }
                                a.push(s.string(i), ':', v);
                                b = true;
                            }
                        }
                    }
                    a[a.length] = '}';
                    return a.join('');
                }
            }
            return 'null';
        },
        'string': function (x) {
            if (/["\\\x00-\x1f]/.test(x)) {
                x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
                    var c = m[b];
                    if (c) {
                        return c;
                    }
                    c = b.charCodeAt();
                    return '\\u00' +
                        Math.floor(c / 16).toString(16) +
                        (c % 16).toString(16);
                });
            }
            return '"' + x + '"';
        }
    };

JWay.toJSON = function(v) {		
	var f;
	if (isNaN(v)) { 
		f = s[typeof v];
	}
	else {
		f = (v instanceof Array) ? s["array"] : s["number"];
	}
	if (f) return f(v);
};

JWay.parseJSON = function(v, safe) {
	if (safe === undefined) safe = JWay.parseJSON.safe;
	if (safe && !/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/.test(v))
		return undefined;
	return eval('('+v+')');
};

JWay.parseJSON.safe = false;

