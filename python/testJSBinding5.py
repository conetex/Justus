#!/mnt/web108/b3/29/5385729/htdocs/_python/bin/python
#!/usr/bin/env python
#!"C:\Program Files\WinPython-32bit-2.7.5.3\python-2.7.5\python.exe"

import js2py
# does not work:
# execjs (PyExecJS)
# (PyV8)

print ("Content-Type: text/html\n")

f = js2py.eval_js( "function f(name) {return name + ' seen in js';}" )
add = js2py.eval_js('function add(a, b) {return a + b}')

message = f("this works | no. 5 | ")
print message

print add(3,4)
