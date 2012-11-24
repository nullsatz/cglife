#!/usr/bin/Rscript

#args <- commandArgs(trailingOnly=T)
#size <- args[1]

library('caTools')
source('life.R')

b <- as.list(rep(NA, 1000))
b[[1]] <- seed_mat(100)
for(i in 2:1000)
	b[[i]] <- life(b[[i - 1]], 1)

c <- array(NA, c(100, 100, 1000))

for(i in 1:1000) { c[, , i] <- b[[i]] }
write.gif(c, 'test.gif', 'jet', 'always', delay=8)
