var rs = require('resemble');
var fs = require('fs');
const path = require('path');
var argv = process.argv.slice(2);
var cwd = process.cwd();


function abs_path(p) {
    if (p === undefined) {
        return undefined;
    }
    if (path.isAbsolute(p)) {
        return p;
    } else {
        return path.resolve(cwd, p);
    }
}



var file1_path = abs_path(argv[0]);
var file2_path = abs_path(argv[1]);

if (file1_path !== undefined && file2_path !== undefined) {
    //console.log(file1_path, file2_path);

    var file1 = fs.readFileSync(file1_path);
    var file2 = fs.readFileSync(file2_path);

    var diff = rs.resemble(file1).compareTo(file2).ignoreColors().ignoreAntialiasing().onComplete(function (data) {
        console.log(data['misMatchPercentage']);
    });
}


