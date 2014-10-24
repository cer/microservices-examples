outputHolder = {
    setDone: function (javaCallback) {
        this.javaCallback = javaCallback;
    }
};

myDustCallback = function (err, out) {
 outputHolder.err = err;
 outputHolder.out = out;
 outputHolder.javaCallback.done(err, out);
}

