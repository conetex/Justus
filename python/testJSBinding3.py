#!/mnt/web108/b3/29/5385729/htdocs/_python/bin/python
#!/usr/bin/env python
#!"C:\Program Files\WinPython-32bit-2.7.5.3\python-2.7.5\python.exe"

import js2py
# does not work:
# execjs (PyExecJS)
# (PyV8)

print ("Content-Type: text/html\n")


js = """
function escramble_758(){
return 55;
}
escramble_758();
"""

message = js2py.eval_js(js)
print message

message2 = " ... this works | no. 3"
print message2
