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

	          var w = iElement[0].offsetWidth;
	          
	          var margin = {top: 20, right: 20, bottom: 30, left: 50},
						    width = w - margin.left - margin.right,
						    height = 500 - margin.top - margin.bottom;

	          
	          
	          
	        // "2015-04-14T22:00:00.000Z"
            var parseDate =  d3.time.format("%Y-%m-%d-%H:%M:%S"),
            	bisectDate = d3.bisector(function(d) { return d.createdDate; }).left;

            var myDateParse = function(date) {
            	if(date instanceof Date)
            		return date;
            	date = date.replace("T","-");
            	//date = date.replace(".000Z","");
            	date = date.split(".")[0];
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
			    .y(function(d) { return y(d[iAttrs.display]); })
			    .interpolate("basis-open");

			svg.attr("width", width + margin.left + margin.right)
			    .attr("height", height + margin.top + margin.bottom)
			  	.append("g")
			    .attr("transform", "translate("+ margin.left +"," + margin.top + ")");

			
			  data.forEach(function(d) {
			    d.createdDate = myDateParse(d.createdDate);
			  });
	
			  x.domain(d3.extent(data, function(d) { return d.createdDate; }));
			  y.domain(d3.extent(data, function(d) { return d[iAttrs.display]; }));
	
			  svg.append("g")
			      .attr("class", "x axis")
			      .attr("transform", "translate(" + margin.left + "," + height + ")")
			      .call(xAxis)
			      .append("Text")
			      .attr("y", 6)
			      .attr("dy", ".71em")
			      .style("text-anchor", "end")
			      .text("Time");
	
			  	svg.append("g")
			      .attr("class", "y axis")
			      .attr("transform", "translate("+ margin.left +",0)")
			      .call(yAxis)
			      .append("text")
			      .attr("transform", "rotate(-90)")
			      .attr("y", 6)
			      .attr("dy", ".71em")
			      .style("text-anchor", "end")
			      .text(iAttrs.display);
	
			  	svg.append("path")
			      .datum(data)
			      .attr("class", "line")
			      .attr("transform", "translate("+ margin.left +",0)")
			      .attr("d", line);
						
			  	var focus = svg.append("g")
			      .attr("class", "focus")
			      .attr("transform", "translate("+ margin.left +",0)")
			      .style("display", "none");
	
			  	focus.append("circle")
			      .attr("r", 4.5);
	
			  	focus.append("text")
			      .attr("x", 9)
			      .attr("dy", ".35em");
	
			  	/* append the x line
			    focus.append("line")
			        .attr("class", "x")
			        .style("stroke", "blue")
			        .style("stroke-dasharray", "3,3")
			        .style("opacity", 0.5)
			        .attr("y1", 0)
			        .attr("y2", height);
	
			    // append the y line
			    focus.append("line")
			        .attr("class", "y")
			        .style("stroke", "blue")
			        .style("stroke-dasharray", "3,3")
			        .style("opacity", 0.5)
			        .attr("x1", 0)
			        .attr("x2", width);*/
			  
			  svg.append("rect")
			      .attr("class", "overlay")
			      .attr("width", width)
			      .attr("height", height)
			      .attr("transform", "translate("+ margin.left +",0)")
			      .on("mouseover", function() { focus.style("display", null); })
			      .on("mouseout", function() { focus.style("display", "none"); })
			      .on("mousemove", mousemove);
	
			  function mousemove() {
			    var x0 = x.invert(d3.mouse(this)[0]),
			        i = bisectDate(data, x0, 1),
			        d0 = data[i - 1],
			        d1 = data[i],
			        d = x0 - d0.createdDate > d1.createdDate - x0 ? d1 : d0;
			    focus.attr("transform", "translate(" + (x(d.createdDate)+ margin.left) +"," + y(d[iAttrs.display]) + ")");
			    focus.select("text").text(d[iAttrs.display]);
			  }
			  
			  
			  
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