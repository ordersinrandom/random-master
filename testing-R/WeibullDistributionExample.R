require(MASS);

x = c(16,53,75,93,34,120, 190);
#x = c(1,2,3,4,5,6,50,51,52,40,50);

fitted = fitdistr(x,"weibull");

# h<-hist(x, breaks=10, col="red", xlab="Time to failure", main="Histogram with Weibull Distribution Fit"); 

xfit = seq(0,max(x)*1.2,length=100); 
yfit = dweibull(xfit,shape=fitted$estimate[1], scale=fitted$estimate[2]); 

# yfit <- yfit*diff(h$mids[1:2])*length(x);


plot(xfit, yfit, "l", col="blue", lwd=3, xlab="Time before failure", ylab="Weibull PDF", main=paste("Weibull shape=", fitted$estimate[1], "scale=", fitted$estimate[2]));
# lines(xfit, yfit, col="blue", lwd=2);
grid();


