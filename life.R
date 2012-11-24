seed_mat <- function(my_dim=10, my_vals=0:1) {
	if(length(my_dim) == 1) {
		data <- sample(my_vals, my_dim^2, T)
		my_obj <- array(data, rep(my_dim, 2))
	} else {
		data <- sample(my_vals, prod(my_dim), T)
		my_obj <- array(data, my_dim)
	}
	return(my_obj)
}

get_nbours <- function(x, i, j) {
	nbours <- rep(NA, 8L)
	if(i != 1L) {
		if(j != 1)
			nbours[1] <- x[i-1L, j-1L]
		nbours[2] <- x[i-1L, j]
		if(j != ncol(x))
			nbours[3] <- x[i-1L, j+1L]
	}
	
	if(j != 1)
		nbours[4] <- x[i, j-1L]
	if(j != ncol(x))
		nbours[5] <- x[i, j+1L]

	if(i != nrow(x)) {
		if(j != 1)
			nbours[6] <- x[i+1L, j-1L]
		nbours[7] <- x[i+1L, j]
		if(j != ncol(x))
			nbours[8] <- x[i+1L, j+1L]
	}
	return(nbours)
}

life <- function(seed, rounds=100L) {
	if(rounds == 0L)
		return(seed)

	updated <- seed
	for(i in 1:nrow(seed)) {
		for(j in 1:ncol(seed)) {
			nbours <- get_nbours(seed, i, j)
			n <- sum(nbours, na.rm=T)
			if((n < 2L) | (n > 3L))
				updated[i, j] <- 0L
			if(n == 3)
				updated[i, j] <- 1L
		}
	}

	life(updated, rounds - 1L)
}
