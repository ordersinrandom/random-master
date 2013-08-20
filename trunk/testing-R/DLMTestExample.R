require(dlm);

# note about using exponential function only for this example
buildFun = function(x) {
   dlmModPoly(1, dV = exp(x[1]), dW = exp(x[2]))
}

# alternative build function 
# that handles the jump at time 1899
#buildFun <- function(x) {
#  m <- dlmModPoly(1, dV = exp(x[1]))
#  m$JW <- matrix(1)
#  m$X <- matrix(exp(x[2]), nc = 1, nr = length(Nile))
#  j <- which(time(Nile) == 1899)
#  m$X[j,1] <- m$X[j,1] * (1 + exp(x[3]))
#  return(m)
#}
#fit = dlmMLE(Nile, parm = c(0,0,0), build = buildFun);

fit = dlmMLE(Nile, parm = c(0,0), build = buildFun);

# construct the model
dlmNile = buildFun(fit$par);

# apply KF
nileFilt = dlmFilter(Nile, dlmNile);


# plotting
plot(Nile, type='o', col ="seagreen");
lines(dropFirst(nileFilt$m), type='o', pch = 20, col="brown");

# plot the SD
attach(nileFilt);
v = unlist(dlmSvd2var(U.C, D.C));
pl = dropFirst(m) + qnorm(0.05, sd = sqrt(v[-1]));
pu = dropFirst(m) + qnorm(0.95, sd = sqrt(v[-1]));
detach();
lines(pl, lty = 2, col = "brown");
lines(pu, lty = 2, col = "brown");
