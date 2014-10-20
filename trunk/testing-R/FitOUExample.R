

ou.likelihood <- function(theta1, theta2, theta3){ 
  n <- length(X) 
  dt <- deltat(X) 
  -sum(dcOU(X[2:n], dt, X[1:(n-1)], c(theta1,theta2,theta3), log=TRUE)) 
} 

require(stats4) 
require(sde) 
set.seed(12333) 
# theta array: first item is mean, 2nd item is reverting rate, 3rd item is vol

X <- sde.sim(model="OU", theta=c(3,1,2), N=250, delta=1) 

mle(ou.likelihood, start=list(theta1=1, theta2=0.5, theta3=1), 
    method="L-BFGS-B", lower=c(-Inf,0,0)) -> fit 

summary(fit) 


plot.ts(X)


