require(dlm);

measurementNoise = 0.1;

targetFunction = function(x) {
  dlm(FF = 1, V = measurementNoise, GG = 1, W = exp(x[1]), m0 = 0, C0 = 1)  
}

dummyTS = as.ts(c(0.39, 0.5, 0.48, 0.29, 0.25, 0.32, 0.34, 0.48, 0.41, 0.45));
#dummyTS = arima.sim(list(order=c(1,0,1), ar=0.5, ma=0.3), n=30);


fit = dlmMLE(dummyTS, parm = c(0), build = targetFunction);

# output some result parameters
fit$conv;

fit$par;

fittedModel = targetFunction(fit$par);

# measure noise estimate
V(fittedModel);

# process noise
W(fittedModel);

# apply Kalman Filter
filtered = dlmFilter(dummyTS, fittedModel);

# plot original TS
plot(dummyTS, type = 'o', col = "seagreen", 
     main="Bilgin's Blog Kalman Filter Example");

abline(v=(seq(0,20,0.5)), col="lightgray", lty="dotted");
abline(h=(seq(0,1,0.05)), col="lightgray", lty="dotted");

# plot filtered estimation
lines(dropFirst(filtered$m), type = 'o', pch = 20, col = "brown");
attach(filtered);
v = unlist(dlmSvd2var(U.C, D.C));
pl = dropFirst(m) + qnorm(0.25, sd = sqrt(v[-1]));
pu = dropFirst(m) + qnorm(0.75, sd = sqrt(v[-1]));
detach();
lines(pl, lty = 2, col = "green");
lines(pu, lty = 2, col = "green");



# dump original TS
dummyTS;
# dump the result TS
dropFirst(filtered$m);
