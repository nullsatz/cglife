def doubler(row):
	doubled = list()
	for e in row:
		doubled.append(e)
		doubled.append(e)
	return doubled

def double_matrix(mat):
	double_mat = list()
	for row in mat:
		double_row = doubler(row)
		double_mat.append(double_row)
		double_mat.append(double_row)
	return double_mat

class Life():

	def __init__(self, nrows = 100, ncols = 100):
		self.dim = (nrows, ncols)
		import random
		random.seed()
		self.history = list()
		self.history.append([map(
			lambda x: random.getrandbits(1), range(ncols)
		) for y in range(nrows)])
		self.time = 0

	def set_nrows(self, nrows):
		old_nrows = self.dim[0]
		self.dim[0] = nrows
		return old_nrows

	def get_nrows(self):
		return self.dim[0]

	def set_ncols(self, ncols):
		old_ncols = self.dim[1]
		self.dim[1] = ncols
		return old_ncols

	def get_ncols(self):
		return self.dim[1]

	def advance_time(self):
		old_time = self.time
		self.time = old_time + 1
		return old_time

	def set_time(self, time):
		old_time = self.time
		self.time = time
		return old_time

	def get_time(self):
		return self.time

	def get_nbor_coords(self, i, j):
		nbor_coords = [[0, 0], ] * 8
		
		nbor_coords[0] = [i - 1, j - 1]
		nbor_coords[1] = [i - 1, j]
		nbor_coords[2] = [i - 1, j + 1]
		
		nbor_coords[3] = [i, j - 1]
		nbor_coords[4] = [i, j + 1]
		
		nbor_coords[5] = [i + 1, j - 1]
		nbor_coords[6] = [i + 1, j]
		nbor_coords[7] = [i + 1, j + 1]
		
		nrows = self.get_nrows()
		ncols = self.get_ncols()
		
		last_row = nrows - 1
		last_col = ncols - 1
		
		if i == 0:
			nbor_coords[0][0] = last_row
			nbor_coords[1][0] = last_row
			nbor_coords[2][0] = last_row
		if i == last_row:
			nbor_coords[5][0] = 0
			nbor_coords[6][0] = 0
			nbor_coords[7][0] = 0
		
		if j == 0:
			nbor_coords[0][1] = last_col
			nbor_coords[3][1] = last_col
			nbor_coords[5][1] = last_col
		if j == last_col:
			nbor_coords[2][1] = 0
			nbor_coords[4][1] = 0
			nbor_coords[7][1] = 0
		return nbor_coords

	def get_nbors(self, time, i, j):
		coords = self.get_nbor_coords(i, j)
		values = [self.history[time][c[0]][c[1]] for c in coords]
		return values

	def advance_history(self):
		nrows = self.get_nrows()
		ncols = self.get_ncols()
		time = self.get_time()
		old_field = self.history[time]
		new_field = [[0, ] * ncols for x in range(nrows)]
		for i in range(nrows):
			for j in range(ncols):
				new_field[i][j] = old_field[i][j]
				nbors = self.get_nbors(time, i, j)
				n = sum(nbors)
				if (n < 2) or (n > 3):
					new_field[i][j] = 0
				if n == 3:
					new_field[i][j] = 1
		self.advance_time()
		self.history.append(new_field)

	def get_color_field(self, time=-1, bg=(0, 0, 255), fg=(0, 255, 0)):
		field = self.history[time]
		nrows = self.get_nrows()
		ncols = self.get_ncols()
		color_field = [[(0, 0, 0) for j in range(ncols)] for i in range(nrows)]
		for i in range(ncols):
			for j in range(nrows):
				if field[i][j] == 0:
					color_field[i][j] = bg
				else:
					color_field[i][j] = fg
		return color_field

	def play(self):
		nrows = self.get_nrows()
		ncols = self.get_ncols()

		import sys, time, pygame

		pygame.init()
		DISPLAYSURF = pygame.display.set_mode((2 * nrows, 2 * ncols), 0, 32)
		pixObj = pygame.PixelArray(DISPLAYSURF)
		
		frames = [self.get_color_field(i) for i in range(len(self.history))]
		frames = [double_matrix(frame) for frame in frames]
	
		for frame in frames:
			for i in range(2 * nrows):
				pixObj[i] = frame[i]
			pygame.display.update()
			time.sleep(0.05)
		del pixObj
