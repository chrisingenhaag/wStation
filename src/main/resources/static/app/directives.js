var wStationDirectives = angular.module('wStation.directives',[ 'wStation.services' ]);

wStationDirectives.directive('d3Lines', ['d3Service', function(d3Service) {
    return {
      restrict: 'EA',
      scope: {
        data: "=",
        label: "@",
        onClick: "&"
      },
      link: function(scope, iElement, iAttrs) {
    	  d3Service.d3().then(function(d3) {
    	  
	        var svg = d3.select(iElement[0])
	            .append("svg")
	            .attr("width", "100%");
	
	        // on window resize, re-render d3 canvas
	        window.onresize = function() {
	          return scope.$apply();
	        };
	        scope.$watch(function(){
	            return angular.element(window)[0].innerWidth;
	          }, function(){
	            return scope.render(scope.data);
	          }
	        );
	
	        // watch for data changes and re-render
	        scope.$watch('data', function(newVals, oldVals) {
	          return scope.render(newVals);
	        }, true);
	
	        // define render function
	        scope.render = function(data){
	          // remove all previous items before render
	          svg.selectAll("*").remove();


						// If we don't pass any data, return out of the element
    				if (!data) return;

	         	var margin = {top: 20, right: 20, bottom: 30, left: 50},
						    width = 960 - margin.left - margin.right,
						    height = 500 - margin.top - margin.bottom;

						    "2015-04-14T22:00:00.000Z"
            var parseDate =  d3.time.format("%Y-%m-%d-%H:%M:%S");

            var myDateParse = function(date) {
            	date = date.replace("T","-");
            	date = date.replace(".000Z","");
            	date = parseDate.parse(date);
            	return date;
            }


						var x = d3.time.scale()
						    .range([0, width]);

						var y = d3.scale.linear()
						    .range([height, 0]);

						var xAxis = d3.svg.axis()
						    .scale(x)
						    .orient("bottom");

						var yAxis = d3.svg.axis()
						    .scale(y)
						    .orient("left");

						var line = d3.svg.line()
						    .x(function(d) { return x(d.createdDate); })
						    .y(function(d) { return y(d.temperature); });

						svg.attr("width", width + margin.left + margin.right)
						    .attr("height", height + margin.top + margin.bottom)
						  	.append("g")
						    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

						
					  data.forEach(function(d) {
					    d.createdDate = myDateParse(d.createdDate);
					    d.temperature = +d.temperature;
					  });

					  x.domain(d3.extent(data, function(d) { return d.createdDate; }));
					  y.domain(d3.extent(data, function(d) { return d.temperature; }));

					  svg.append("g")
					      .attr("class", "x axis")
					      .attr("transform", "translate(0," + height + ")")
					      .call(xAxis);

					  svg.append("g")
					      .attr("class", "y axis")
					      .call(yAxis)
					    .append("text")
					      .attr("transform", "rotate(-90)")
					      .attr("y", 6)
					      .attr("dy", ".71em")
					      .style("text-anchor", "end")
					      .text("Price ($)");

					  svg.append("path")
					      .datum(data)
					      .attr("class", "line")
					      .attr("d", line);
					

	        };
    	  });
      }
    };
  }]);


wStationDirectives.directive('d3Bars', ['d3Service', function(d3Service) {
    return {
      restrict: 'EA',
      scope: {
        data: "=",
        label: "@",
        onClick: "&"
      },
      link: function(scope, iElement, iAttrs) {
    	  d3Service.d3().then(function(d3) {
    	  
	        var svg = d3.select(iElement[0])
	            .append("svg")
	            .attr("width", "100%");
	
	        // on window resize, re-render d3 canvas
	        window.onresize = function() {
	          return scope.$apply();
	        };
	        scope.$watch(function(){
	            return angular.element(window)[0].innerWidth;
	          }, function(){
	            return scope.render(scope.data);
	          }
	        );
	
	        // watch for data changes and re-render
	        scope.$watch('data', function(newVals, oldVals) {
	          return scope.render(newVals);
	        }, true);
	
	        // define render function
	        scope.render = function(data){
	          // remove all previous items before render
	          svg.selectAll("*").remove();
	
	          // setup variables
	          var width, height, max;
	          width = d3.select(iElement[0])[0][0].offsetWidth - 20;
	            // 20 is for margins and can be changed
	          height = scope.data.length * 35;
	            // 35 = 30(bar height) + 5(margin between bars)
	          max = 98;
	            // this can also be found dynamically when the data is not static
	            // max = Math.max.apply(Math, _.map(data, ((val)-> val.count)))
	
	          // set the height based on the calculations above
	          svg.attr('height', height);
	
	          //create the rectangles for the bar chart
	          svg.selectAll("rect")
	            .data(data)
	            .enter()
	              .append("rect")
	              .on("click", function(d, i){return scope.onClick({item: d});})
	              .attr("height", 30) // height of each bar
	              .attr("width", 0) // initial width of 0 for transition
	              .attr("x", 10) // half of the 20 side margin specified above
	              .attr("y", function(d, i){
	                return i * 35;
	              }) // height + margin between bars
	              .transition()
	                .duration(1000) // time of duration
	                .attr("width", function(d){
	                  return d.score/(max/width);
	                }); // width based on scale
	
	          svg.selectAll("text")
	            .data(data)
	            .enter()
	              .append("text")
	              .attr("fill", "#fff")
	              .attr("y", function(d, i){return i * 35 + 22;})
	              .attr("x", 15)
	              .text(function(d){return d[scope.label];});

	        };
    	  });
      }
    };
  }]);