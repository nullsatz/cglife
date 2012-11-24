#!/opt/local/bin/python
from life import Life
a = Life(200, 200)
for i in range(1000):
	a.advance_history()
a.play()
