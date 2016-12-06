# Bus Route Challenge

### Solution

Technology stack: Spring boot & Java 8 & gs-collections & Guava (mainly for cache) & Maven & Swagger2

Kindly increase sleep time in `tests/simple_test.sh` because
startup application depends on input file size. It takes from 5 seconds to one minute to start application. 

Solution is optimized for large files. **Gs-collections** has been used to optimize memory usage.
Files `screenshots/heap.png` and `screenshots/heap_dump.png` show heap usage for 700MB input file (task upper limit)

Details of algorithm **boolean isDirectRoute(int departureId, int arrivalId)**
1. There is additional precomputed map[bus station -> sorted routes] to speed up checking direct routes
2. For above map: departureSortedRoutes = map.get(departureId) & arrivalSortedRoutes = map.get(arrivalId)
3. Find intersectRoutes(departureSortedRoutes, arrivalSortedRoutes). Time complexity is O(n+m) because collections are sorted.
4. For above result check if any route contains both stations
5. Declarative cache @Cacheable("directRoutes") has been added

I have done some performance tests with ApacheBench with declarative cache turned off. 
You can find results in `screenshots` directory.

