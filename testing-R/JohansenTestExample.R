require(vars) 
require(urca) 
reps <- 60 # length of time series 
A <- matrix(NA,nrow=reps,ncol=3) 
colnames(A) <- c("a","b","c") 
A[1,] <- rnorm(3) # starting values 
for(i in 2:reps){# generate time series 
  A[i,] <- c(0.1+0.2*i+0.7*A[i-1,1]+0.1*A[i-1,2]+0.1*A[i-1,3]+rnorm(1)*50, 
             0.5+0.1*i+0.6*A[i-1,2]-0.2*A[i-1,1]-0.2*A[i-1,3]+rnorm(1)*130, 
             0.9+0.2*i+A[i-1,3]+0.1*A[i-1,1]+0.15*A[i-1,2]+rnorm(1)*80 
  ) 
} 
(a.ct <- ur.df(A[,"a"],type="trend")) 
(b.ct <- ur.df(A[,"b"],type="trend")) 
(c.ct <- ur.df(A[,"c"],type="trend")) 

VARselect(A,type="both") 
var.p1 <- VAR(A,1,type="both") 
summary(var.p1) 
jo <- ca.jo(A) 
summary(jo) 


# Code From Paul BELOW

#str(jo)

# capture the cointegrated values
ResultBeta = matrix(NA, nrow=2, ncol=3)
for (i in 1:2) {
  for (j in 1:3) {
    ResultBeta[i,j] = jo@V[i+1,j]
  }
}

# create the cointegrated time series
B = matrix(NA, nrow = reps, ncol=3)
for (i in 1:reps) {
  for (j in 1:3) {
    B[i,j] = A[i,1] + ResultBeta[1,j] * A[i, 2] + ResultBeta[2,j] * A[i,3]
  }
}

plot.ts(A)
plot.ts(B)
