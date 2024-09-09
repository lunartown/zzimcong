/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
$(document).ready(function() {

    $(".click-title").mouseenter( function(    e){
        e.preventDefault();
        this.style.cursor="pointer";
    });
    $(".click-title").mousedown( function(event){
        event.preventDefault();
    });

    // Ugly code while this script is shared among several pages
    try{
        refreshHitsPerSecond(true);
    } catch(e){}
    try{
        refreshResponseTimeOverTime(true);
    } catch(e){}
    try{
        refreshResponseTimePercentiles();
    } catch(e){}
});


var responseTimePercentilesInfos = {
        data: {"result": {"minY": 24.0, "minX": 0.0, "maxY": 1045.0, "series": [{"data": [[0.0, 24.0], [0.1, 25.0], [0.2, 26.0], [0.3, 26.0], [0.4, 28.0], [0.5, 28.0], [0.6, 28.0], [0.7, 29.0], [0.8, 29.0], [0.9, 29.0], [1.0, 30.0], [1.1, 31.0], [1.2, 31.0], [1.3, 31.0], [1.4, 31.0], [1.5, 32.0], [1.6, 32.0], [1.7, 32.0], [1.8, 32.0], [1.9, 32.0], [2.0, 32.0], [2.1, 32.0], [2.2, 33.0], [2.3, 33.0], [2.4, 33.0], [2.5, 33.0], [2.6, 33.0], [2.7, 34.0], [2.8, 34.0], [2.9, 34.0], [3.0, 34.0], [3.1, 34.0], [3.2, 34.0], [3.3, 35.0], [3.4, 35.0], [3.5, 35.0], [3.6, 35.0], [3.7, 36.0], [3.8, 36.0], [3.9, 36.0], [4.0, 36.0], [4.1, 36.0], [4.2, 36.0], [4.3, 37.0], [4.4, 37.0], [4.5, 37.0], [4.6, 37.0], [4.7, 37.0], [4.8, 38.0], [4.9, 38.0], [5.0, 38.0], [5.1, 38.0], [5.2, 38.0], [5.3, 38.0], [5.4, 39.0], [5.5, 39.0], [5.6, 39.0], [5.7, 39.0], [5.8, 39.0], [5.9, 39.0], [6.0, 39.0], [6.1, 39.0], [6.2, 40.0], [6.3, 40.0], [6.4, 40.0], [6.5, 40.0], [6.6, 40.0], [6.7, 40.0], [6.8, 41.0], [6.9, 41.0], [7.0, 41.0], [7.1, 41.0], [7.2, 41.0], [7.3, 41.0], [7.4, 41.0], [7.5, 41.0], [7.6, 41.0], [7.7, 42.0], [7.8, 42.0], [7.9, 42.0], [8.0, 42.0], [8.1, 42.0], [8.2, 42.0], [8.3, 42.0], [8.4, 43.0], [8.5, 43.0], [8.6, 43.0], [8.7, 43.0], [8.8, 43.0], [8.9, 43.0], [9.0, 43.0], [9.1, 43.0], [9.2, 44.0], [9.3, 44.0], [9.4, 44.0], [9.5, 44.0], [9.6, 44.0], [9.7, 44.0], [9.8, 44.0], [9.9, 44.0], [10.0, 45.0], [10.1, 45.0], [10.2, 45.0], [10.3, 45.0], [10.4, 45.0], [10.5, 45.0], [10.6, 45.0], [10.7, 45.0], [10.8, 45.0], [10.9, 45.0], [11.0, 46.0], [11.1, 46.0], [11.2, 46.0], [11.3, 46.0], [11.4, 46.0], [11.5, 46.0], [11.6, 46.0], [11.7, 46.0], [11.8, 46.0], [11.9, 46.0], [12.0, 47.0], [12.1, 47.0], [12.2, 47.0], [12.3, 47.0], [12.4, 47.0], [12.5, 47.0], [12.6, 47.0], [12.7, 47.0], [12.8, 47.0], [12.9, 47.0], [13.0, 47.0], [13.1, 47.0], [13.2, 47.0], [13.3, 48.0], [13.4, 48.0], [13.5, 48.0], [13.6, 48.0], [13.7, 48.0], [13.8, 48.0], [13.9, 48.0], [14.0, 48.0], [14.1, 48.0], [14.2, 49.0], [14.3, 49.0], [14.4, 49.0], [14.5, 49.0], [14.6, 49.0], [14.7, 49.0], [14.8, 49.0], [14.9, 50.0], [15.0, 50.0], [15.1, 50.0], [15.2, 50.0], [15.3, 50.0], [15.4, 50.0], [15.5, 50.0], [15.6, 50.0], [15.7, 50.0], [15.8, 50.0], [15.9, 50.0], [16.0, 50.0], [16.1, 51.0], [16.2, 51.0], [16.3, 51.0], [16.4, 51.0], [16.5, 51.0], [16.6, 51.0], [16.7, 51.0], [16.8, 51.0], [16.9, 51.0], [17.0, 52.0], [17.1, 52.0], [17.2, 52.0], [17.3, 52.0], [17.4, 52.0], [17.5, 52.0], [17.6, 52.0], [17.7, 52.0], [17.8, 52.0], [17.9, 52.0], [18.0, 52.0], [18.1, 53.0], [18.2, 53.0], [18.3, 53.0], [18.4, 53.0], [18.5, 53.0], [18.6, 53.0], [18.7, 53.0], [18.8, 53.0], [18.9, 53.0], [19.0, 53.0], [19.1, 53.0], [19.2, 53.0], [19.3, 53.0], [19.4, 54.0], [19.5, 54.0], [19.6, 54.0], [19.7, 54.0], [19.8, 54.0], [19.9, 54.0], [20.0, 54.0], [20.1, 54.0], [20.2, 54.0], [20.3, 55.0], [20.4, 55.0], [20.5, 55.0], [20.6, 55.0], [20.7, 55.0], [20.8, 55.0], [20.9, 55.0], [21.0, 55.0], [21.1, 56.0], [21.2, 56.0], [21.3, 56.0], [21.4, 56.0], [21.5, 56.0], [21.6, 56.0], [21.7, 57.0], [21.8, 57.0], [21.9, 57.0], [22.0, 57.0], [22.1, 57.0], [22.2, 57.0], [22.3, 57.0], [22.4, 57.0], [22.5, 57.0], [22.6, 57.0], [22.7, 57.0], [22.8, 58.0], [22.9, 58.0], [23.0, 58.0], [23.1, 58.0], [23.2, 58.0], [23.3, 58.0], [23.4, 58.0], [23.5, 58.0], [23.6, 58.0], [23.7, 58.0], [23.8, 59.0], [23.9, 59.0], [24.0, 59.0], [24.1, 59.0], [24.2, 59.0], [24.3, 59.0], [24.4, 59.0], [24.5, 60.0], [24.6, 60.0], [24.7, 60.0], [24.8, 60.0], [24.9, 60.0], [25.0, 60.0], [25.1, 60.0], [25.2, 60.0], [25.3, 61.0], [25.4, 61.0], [25.5, 61.0], [25.6, 61.0], [25.7, 61.0], [25.8, 61.0], [25.9, 62.0], [26.0, 62.0], [26.1, 62.0], [26.2, 62.0], [26.3, 62.0], [26.4, 62.0], [26.5, 62.0], [26.6, 62.0], [26.7, 63.0], [26.8, 63.0], [26.9, 63.0], [27.0, 63.0], [27.1, 63.0], [27.2, 63.0], [27.3, 63.0], [27.4, 63.0], [27.5, 64.0], [27.6, 64.0], [27.7, 64.0], [27.8, 64.0], [27.9, 64.0], [28.0, 64.0], [28.1, 64.0], [28.2, 64.0], [28.3, 65.0], [28.4, 65.0], [28.5, 65.0], [28.6, 65.0], [28.7, 65.0], [28.8, 65.0], [28.9, 65.0], [29.0, 66.0], [29.1, 66.0], [29.2, 66.0], [29.3, 66.0], [29.4, 66.0], [29.5, 66.0], [29.6, 66.0], [29.7, 67.0], [29.8, 67.0], [29.9, 67.0], [30.0, 67.0], [30.1, 67.0], [30.2, 67.0], [30.3, 67.0], [30.4, 68.0], [30.5, 68.0], [30.6, 68.0], [30.7, 68.0], [30.8, 68.0], [30.9, 68.0], [31.0, 68.0], [31.1, 69.0], [31.2, 69.0], [31.3, 69.0], [31.4, 69.0], [31.5, 69.0], [31.6, 69.0], [31.7, 70.0], [31.8, 70.0], [31.9, 70.0], [32.0, 70.0], [32.1, 70.0], [32.2, 71.0], [32.3, 71.0], [32.4, 71.0], [32.5, 71.0], [32.6, 71.0], [32.7, 72.0], [32.8, 72.0], [32.9, 72.0], [33.0, 72.0], [33.1, 73.0], [33.2, 73.0], [33.3, 73.0], [33.4, 73.0], [33.5, 73.0], [33.6, 73.0], [33.7, 73.0], [33.8, 73.0], [33.9, 73.0], [34.0, 73.0], [34.1, 74.0], [34.2, 74.0], [34.3, 74.0], [34.4, 74.0], [34.5, 74.0], [34.6, 75.0], [34.7, 75.0], [34.8, 75.0], [34.9, 75.0], [35.0, 75.0], [35.1, 75.0], [35.2, 76.0], [35.3, 76.0], [35.4, 76.0], [35.5, 76.0], [35.6, 76.0], [35.7, 76.0], [35.8, 76.0], [35.9, 76.0], [36.0, 76.0], [36.1, 77.0], [36.2, 77.0], [36.3, 77.0], [36.4, 77.0], [36.5, 77.0], [36.6, 77.0], [36.7, 77.0], [36.8, 77.0], [36.9, 77.0], [37.0, 78.0], [37.1, 78.0], [37.2, 78.0], [37.3, 78.0], [37.4, 78.0], [37.5, 79.0], [37.6, 79.0], [37.7, 79.0], [37.8, 79.0], [37.9, 79.0], [38.0, 79.0], [38.1, 79.0], [38.2, 79.0], [38.3, 80.0], [38.4, 80.0], [38.5, 80.0], [38.6, 80.0], [38.7, 80.0], [38.8, 80.0], [38.9, 80.0], [39.0, 81.0], [39.1, 81.0], [39.2, 81.0], [39.3, 81.0], [39.4, 81.0], [39.5, 81.0], [39.6, 81.0], [39.7, 81.0], [39.8, 82.0], [39.9, 82.0], [40.0, 82.0], [40.1, 82.0], [40.2, 82.0], [40.3, 83.0], [40.4, 83.0], [40.5, 83.0], [40.6, 83.0], [40.7, 83.0], [40.8, 83.0], [40.9, 84.0], [41.0, 84.0], [41.1, 84.0], [41.2, 84.0], [41.3, 84.0], [41.4, 84.0], [41.5, 85.0], [41.6, 85.0], [41.7, 85.0], [41.8, 85.0], [41.9, 86.0], [42.0, 86.0], [42.1, 86.0], [42.2, 86.0], [42.3, 86.0], [42.4, 87.0], [42.5, 87.0], [42.6, 87.0], [42.7, 88.0], [42.8, 88.0], [42.9, 88.0], [43.0, 88.0], [43.1, 88.0], [43.2, 88.0], [43.3, 88.0], [43.4, 89.0], [43.5, 89.0], [43.6, 89.0], [43.7, 89.0], [43.8, 89.0], [43.9, 90.0], [44.0, 90.0], [44.1, 90.0], [44.2, 91.0], [44.3, 91.0], [44.4, 91.0], [44.5, 91.0], [44.6, 92.0], [44.7, 92.0], [44.8, 92.0], [44.9, 93.0], [45.0, 93.0], [45.1, 93.0], [45.2, 94.0], [45.3, 94.0], [45.4, 94.0], [45.5, 94.0], [45.6, 94.0], [45.7, 95.0], [45.8, 95.0], [45.9, 96.0], [46.0, 96.0], [46.1, 96.0], [46.2, 96.0], [46.3, 96.0], [46.4, 97.0], [46.5, 98.0], [46.6, 98.0], [46.7, 98.0], [46.8, 99.0], [46.9, 100.0], [47.0, 100.0], [47.1, 100.0], [47.2, 100.0], [47.3, 101.0], [47.4, 102.0], [47.5, 102.0], [47.6, 102.0], [47.7, 102.0], [47.8, 102.0], [47.9, 103.0], [48.0, 103.0], [48.1, 103.0], [48.2, 103.0], [48.3, 104.0], [48.4, 104.0], [48.5, 104.0], [48.6, 105.0], [48.7, 106.0], [48.8, 106.0], [48.9, 106.0], [49.0, 107.0], [49.1, 107.0], [49.2, 107.0], [49.3, 107.0], [49.4, 108.0], [49.5, 108.0], [49.6, 108.0], [49.7, 109.0], [49.8, 110.0], [49.9, 110.0], [50.0, 110.0], [50.1, 111.0], [50.2, 112.0], [50.3, 112.0], [50.4, 112.0], [50.5, 113.0], [50.6, 113.0], [50.7, 113.0], [50.8, 114.0], [50.9, 114.0], [51.0, 114.0], [51.1, 115.0], [51.2, 115.0], [51.3, 116.0], [51.4, 116.0], [51.5, 117.0], [51.6, 117.0], [51.7, 118.0], [51.8, 118.0], [51.9, 118.0], [52.0, 119.0], [52.1, 119.0], [52.2, 120.0], [52.3, 120.0], [52.4, 120.0], [52.5, 121.0], [52.6, 121.0], [52.7, 121.0], [52.8, 122.0], [52.9, 123.0], [53.0, 123.0], [53.1, 123.0], [53.2, 123.0], [53.3, 124.0], [53.4, 125.0], [53.5, 125.0], [53.6, 125.0], [53.7, 126.0], [53.8, 126.0], [53.9, 126.0], [54.0, 127.0], [54.1, 128.0], [54.2, 128.0], [54.3, 128.0], [54.4, 128.0], [54.5, 129.0], [54.6, 129.0], [54.7, 130.0], [54.8, 130.0], [54.9, 131.0], [55.0, 132.0], [55.1, 132.0], [55.2, 132.0], [55.3, 132.0], [55.4, 133.0], [55.5, 134.0], [55.6, 134.0], [55.7, 134.0], [55.8, 135.0], [55.9, 135.0], [56.0, 136.0], [56.1, 136.0], [56.2, 137.0], [56.3, 138.0], [56.4, 139.0], [56.5, 139.0], [56.6, 139.0], [56.7, 141.0], [56.8, 141.0], [56.9, 141.0], [57.0, 142.0], [57.1, 142.0], [57.2, 142.0], [57.3, 143.0], [57.4, 144.0], [57.5, 144.0], [57.6, 145.0], [57.7, 145.0], [57.8, 146.0], [57.9, 146.0], [58.0, 148.0], [58.1, 148.0], [58.2, 150.0], [58.3, 151.0], [58.4, 152.0], [58.5, 152.0], [58.6, 153.0], [58.7, 154.0], [58.8, 155.0], [58.9, 155.0], [59.0, 155.0], [59.1, 156.0], [59.2, 157.0], [59.3, 157.0], [59.4, 158.0], [59.5, 158.0], [59.6, 159.0], [59.7, 160.0], [59.8, 160.0], [59.9, 161.0], [60.0, 161.0], [60.1, 162.0], [60.2, 162.0], [60.3, 163.0], [60.4, 164.0], [60.5, 165.0], [60.6, 166.0], [60.7, 166.0], [60.8, 166.0], [60.9, 167.0], [61.0, 168.0], [61.1, 168.0], [61.2, 169.0], [61.3, 169.0], [61.4, 171.0], [61.5, 171.0], [61.6, 172.0], [61.7, 173.0], [61.8, 173.0], [61.9, 174.0], [62.0, 174.0], [62.1, 175.0], [62.2, 175.0], [62.3, 175.0], [62.4, 177.0], [62.5, 178.0], [62.6, 178.0], [62.7, 179.0], [62.8, 179.0], [62.9, 180.0], [63.0, 181.0], [63.1, 181.0], [63.2, 182.0], [63.3, 182.0], [63.4, 183.0], [63.5, 183.0], [63.6, 184.0], [63.7, 186.0], [63.8, 186.0], [63.9, 187.0], [64.0, 187.0], [64.1, 189.0], [64.2, 189.0], [64.3, 190.0], [64.4, 191.0], [64.5, 192.0], [64.6, 192.0], [64.7, 193.0], [64.8, 194.0], [64.9, 195.0], [65.0, 195.0], [65.1, 196.0], [65.2, 197.0], [65.3, 198.0], [65.4, 199.0], [65.5, 199.0], [65.6, 200.0], [65.7, 201.0], [65.8, 202.0], [65.9, 202.0], [66.0, 204.0], [66.1, 204.0], [66.2, 205.0], [66.3, 205.0], [66.4, 207.0], [66.5, 208.0], [66.6, 209.0], [66.7, 209.0], [66.8, 211.0], [66.9, 212.0], [67.0, 212.0], [67.1, 213.0], [67.2, 215.0], [67.3, 216.0], [67.4, 216.0], [67.5, 217.0], [67.6, 217.0], [67.7, 218.0], [67.8, 218.0], [67.9, 219.0], [68.0, 219.0], [68.1, 219.0], [68.2, 221.0], [68.3, 221.0], [68.4, 222.0], [68.5, 223.0], [68.6, 223.0], [68.7, 223.0], [68.8, 224.0], [68.9, 225.0], [69.0, 225.0], [69.1, 226.0], [69.2, 226.0], [69.3, 227.0], [69.4, 229.0], [69.5, 229.0], [69.6, 231.0], [69.7, 232.0], [69.8, 232.0], [69.9, 234.0], [70.0, 235.0], [70.1, 237.0], [70.2, 238.0], [70.3, 239.0], [70.4, 241.0], [70.5, 241.0], [70.6, 241.0], [70.7, 242.0], [70.8, 243.0], [70.9, 244.0], [71.0, 245.0], [71.1, 245.0], [71.2, 247.0], [71.3, 248.0], [71.4, 248.0], [71.5, 249.0], [71.6, 250.0], [71.7, 250.0], [71.8, 251.0], [71.9, 252.0], [72.0, 254.0], [72.1, 254.0], [72.2, 255.0], [72.3, 257.0], [72.4, 257.0], [72.5, 258.0], [72.6, 258.0], [72.7, 259.0], [72.8, 260.0], [72.9, 262.0], [73.0, 263.0], [73.1, 265.0], [73.2, 266.0], [73.3, 269.0], [73.4, 271.0], [73.5, 274.0], [73.6, 275.0], [73.7, 276.0], [73.8, 277.0], [73.9, 279.0], [74.0, 282.0], [74.1, 283.0], [74.2, 283.0], [74.3, 284.0], [74.4, 287.0], [74.5, 287.0], [74.6, 289.0], [74.7, 290.0], [74.8, 291.0], [74.9, 293.0], [75.0, 295.0], [75.1, 297.0], [75.2, 298.0], [75.3, 298.0], [75.4, 300.0], [75.5, 301.0], [75.6, 302.0], [75.7, 302.0], [75.8, 302.0], [75.9, 303.0], [76.0, 305.0], [76.1, 306.0], [76.2, 307.0], [76.3, 309.0], [76.4, 309.0], [76.5, 310.0], [76.6, 311.0], [76.7, 312.0], [76.8, 313.0], [76.9, 315.0], [77.0, 316.0], [77.1, 317.0], [77.2, 317.0], [77.3, 318.0], [77.4, 319.0], [77.5, 320.0], [77.6, 320.0], [77.7, 322.0], [77.8, 325.0], [77.9, 325.0], [78.0, 326.0], [78.1, 327.0], [78.2, 328.0], [78.3, 330.0], [78.4, 332.0], [78.5, 333.0], [78.6, 335.0], [78.7, 335.0], [78.8, 337.0], [78.9, 338.0], [79.0, 339.0], [79.1, 340.0], [79.2, 341.0], [79.3, 343.0], [79.4, 345.0], [79.5, 346.0], [79.6, 347.0], [79.7, 348.0], [79.8, 349.0], [79.9, 349.0], [80.0, 350.0], [80.1, 351.0], [80.2, 353.0], [80.3, 354.0], [80.4, 355.0], [80.5, 355.0], [80.6, 356.0], [80.7, 357.0], [80.8, 358.0], [80.9, 358.0], [81.0, 359.0], [81.1, 359.0], [81.2, 361.0], [81.3, 362.0], [81.4, 363.0], [81.5, 364.0], [81.6, 364.0], [81.7, 366.0], [81.8, 367.0], [81.9, 371.0], [82.0, 371.0], [82.1, 372.0], [82.2, 372.0], [82.3, 373.0], [82.4, 373.0], [82.5, 374.0], [82.6, 375.0], [82.7, 376.0], [82.8, 377.0], [82.9, 378.0], [83.0, 379.0], [83.1, 380.0], [83.2, 382.0], [83.3, 384.0], [83.4, 385.0], [83.5, 387.0], [83.6, 388.0], [83.7, 388.0], [83.8, 389.0], [83.9, 389.0], [84.0, 390.0], [84.1, 390.0], [84.2, 391.0], [84.3, 392.0], [84.4, 393.0], [84.5, 393.0], [84.6, 393.0], [84.7, 395.0], [84.8, 396.0], [84.9, 397.0], [85.0, 398.0], [85.1, 398.0], [85.2, 400.0], [85.3, 402.0], [85.4, 402.0], [85.5, 404.0], [85.6, 405.0], [85.7, 406.0], [85.8, 407.0], [85.9, 408.0], [86.0, 409.0], [86.1, 410.0], [86.2, 413.0], [86.3, 413.0], [86.4, 414.0], [86.5, 416.0], [86.6, 416.0], [86.7, 417.0], [86.8, 418.0], [86.9, 418.0], [87.0, 419.0], [87.1, 419.0], [87.2, 420.0], [87.3, 420.0], [87.4, 423.0], [87.5, 424.0], [87.6, 425.0], [87.7, 426.0], [87.8, 427.0], [87.9, 428.0], [88.0, 429.0], [88.1, 431.0], [88.2, 432.0], [88.3, 435.0], [88.4, 436.0], [88.5, 437.0], [88.6, 439.0], [88.7, 441.0], [88.8, 442.0], [88.9, 444.0], [89.0, 444.0], [89.1, 445.0], [89.2, 447.0], [89.3, 448.0], [89.4, 450.0], [89.5, 451.0], [89.6, 452.0], [89.7, 453.0], [89.8, 454.0], [89.9, 454.0], [90.0, 455.0], [90.1, 456.0], [90.2, 456.0], [90.3, 458.0], [90.4, 459.0], [90.5, 459.0], [90.6, 461.0], [90.7, 462.0], [90.8, 463.0], [90.9, 463.0], [91.0, 464.0], [91.1, 465.0], [91.2, 466.0], [91.3, 467.0], [91.4, 468.0], [91.5, 468.0], [91.6, 469.0], [91.7, 471.0], [91.8, 471.0], [91.9, 472.0], [92.0, 472.0], [92.1, 474.0], [92.2, 475.0], [92.3, 476.0], [92.4, 476.0], [92.5, 477.0], [92.6, 478.0], [92.7, 480.0], [92.8, 482.0], [92.9, 483.0], [93.0, 484.0], [93.1, 488.0], [93.2, 489.0], [93.3, 492.0], [93.4, 492.0], [93.5, 493.0], [93.6, 493.0], [93.7, 495.0], [93.8, 499.0], [93.9, 501.0], [94.0, 503.0], [94.1, 506.0], [94.2, 506.0], [94.3, 510.0], [94.4, 515.0], [94.5, 516.0], [94.6, 518.0], [94.7, 519.0], [94.8, 521.0], [94.9, 523.0], [95.0, 524.0], [95.1, 525.0], [95.2, 527.0], [95.3, 529.0], [95.4, 532.0], [95.5, 533.0], [95.6, 534.0], [95.7, 536.0], [95.8, 541.0], [95.9, 543.0], [96.0, 547.0], [96.1, 552.0], [96.2, 560.0], [96.3, 566.0], [96.4, 569.0], [96.5, 573.0], [96.6, 575.0], [96.7, 577.0], [96.8, 582.0], [96.9, 584.0], [97.0, 588.0], [97.1, 592.0], [97.2, 598.0], [97.3, 606.0], [97.4, 609.0], [97.5, 623.0], [97.6, 630.0], [97.7, 635.0], [97.8, 637.0], [97.9, 644.0], [98.0, 664.0], [98.1, 672.0], [98.2, 683.0], [98.3, 688.0], [98.4, 695.0], [98.5, 712.0], [98.6, 727.0], [98.7, 743.0], [98.8, 751.0], [98.9, 755.0], [99.0, 762.0], [99.1, 775.0], [99.2, 787.0], [99.3, 823.0], [99.4, 848.0], [99.5, 888.0], [99.6, 937.0], [99.7, 950.0], [99.8, 990.0], [99.9, 1032.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 100.0, "title": "Response Time Percentiles"}},
        getOptions: function() {
            return {
                series: {
                    points: { show: false }
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentiles'
                },
                xaxis: {
                    tickDecimals: 1,
                    axisLabel: "Percentiles",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Percentile value in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : %x.2 percentile was %y ms"
                },
                selection: { mode: "xy" },
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentiles"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesPercentiles"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesPercentiles"), dataset, prepareOverviewOptions(options));
        }
};

/**
 * @param elementId Id of element where we display message
 */
function setEmptyGraph(elementId) {
    $(function() {
        $(elementId).text("No graph series with filter="+seriesFilter);
    });
}

// Response times percentiles
function refreshResponseTimePercentiles() {
    var infos = responseTimePercentilesInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimePercentiles");
        return;
    }
    if (isGraph($("#flotResponseTimesPercentiles"))){
        infos.createGraph();
    } else {
        var choiceContainer = $("#choicesResponseTimePercentiles");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesPercentiles", "#overviewResponseTimesPercentiles");
        $('#bodyResponseTimePercentiles .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimeDistributionInfos = {
        data: {"result": {"minY": 3.0, "minX": 0.0, "maxY": 937.0, "series": [{"data": [[0.0, 937.0], [300.0, 196.0], [600.0, 24.0], [700.0, 16.0], [400.0, 173.0], [100.0, 373.0], [200.0, 198.0], [800.0, 7.0], [900.0, 5.0], [500.0, 68.0], [1000.0, 3.0]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 100, "maxX": 1000.0, "title": "Response Time Distribution"}},
        getOptions: function() {
            var granularity = this.data.result.granularity;
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    barWidth: this.data.result.granularity
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " responses for " + label + " were between " + xval + " and " + (xval + granularity) + " ms";
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimeDistribution"), prepareData(data.result.series, $("#choicesResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshResponseTimeDistribution() {
    var infos = responseTimeDistributionInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeDistribution");
        return;
    }
    if (isGraph($("#flotResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var syntheticResponseTimeDistributionInfos = {
        data: {"result": {"minY": 123.0, "minX": 0.0, "ticks": [[0, "요청 수 \n(응답시간 <= 500ms)"], [1, "요청 수 \n(응답시간 > 500ms 및 <= 1,500ms)"], [2, "요청 수 \n(응답시간 > 1,500ms)"], [3, "오류 발생 요청"]], "maxY": 1877.0, "series": [{"data": [[0.0, 1877.0]], "color": "#9ACD32", "isOverall": false, "label": "요청 수 \n(응답시간 <= 500ms)", "isController": false}, {"data": [[1.0, 123.0]], "color": "yellow", "isOverall": false, "label": "요청 수 \n(응답시간 > 500ms 및 <= 1,500ms)", "isController": false}, {"data": [], "color": "orange", "isOverall": false, "label": "요청 수 \n(응답시간 > 1,500ms)", "isController": false}, {"data": [], "color": "#FF6347", "isOverall": false, "label": "오류 발생 요청", "isController": false}], "supportsControllersDiscrimination": false, "maxX": 1.0, "title": "Synthetic Response Times Distribution"}},
        getOptions: function() {
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendSyntheticResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times ranges",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                    tickLength:0,
                    min:-0.5,
                    max:3.5
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    align: "center",
                    barWidth: 0.25,
                    fill:.75
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " " + label;
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            options.xaxis.ticks = data.result.ticks;
            $.plot($("#flotSyntheticResponseTimeDistribution"), prepareData(data.result.series, $("#choicesSyntheticResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshSyntheticResponseTimeDistribution() {
    var infos = syntheticResponseTimeDistributionInfos;
    prepareSeries(infos.data, true);
    if (isGraph($("#flotSyntheticResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerSyntheticResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var activeThreadsOverTimeInfos = {
        data: {"result": {"minY": 39.53100000000002, "minX": 1.72493424E12, "maxY": 39.53100000000002, "series": [{"data": [[1.72493424E12, 39.53100000000002]], "isOverall": false, "label": "Thread Group", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.72493424E12, "title": "Active Threads Over Time"}},
        getOptions: function() {
            return {
                series: {
                    stack: true,
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 6,
                    show: true,
                    container: '#legendActiveThreadsOverTime'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                selection: {
                    mode: 'xy'
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : At %x there were %y active threads"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesActiveThreadsOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotActiveThreadsOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewActiveThreadsOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Active Threads Over Time
function refreshActiveThreadsOverTime(fixTimestamps) {
    var infos = activeThreadsOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotActiveThreadsOverTime"))) {
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesActiveThreadsOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotActiveThreadsOverTime", "#overviewActiveThreadsOverTime");
        $('#footerActiveThreadsOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var timeVsThreadsInfos = {
        data: {"result": {"minY": 32.666666666666664, "minX": 2.0, "maxY": 662.0, "series": [{"data": [[2.0, 35.5], [3.0, 39.0], [4.0, 37.0], [6.0, 32.666666666666664], [7.0, 60.83333333333333], [8.0, 49.625], [9.0, 46.57894736842105], [10.0, 48.68750000000001], [11.0, 56.27536231884058], [12.0, 56.0344827586207], [13.0, 55.91428571428571], [14.0, 61.12765957446808], [15.0, 66.66666666666666], [16.0, 76.86666666666666], [17.0, 80.26923076923079], [18.0, 73.0], [19.0, 70.69565217391305], [20.0, 97.55555555555554], [21.0, 70.3], [22.0, 88.64285714285712], [23.0, 98.15384615384616], [24.0, 131.56818181818178], [25.0, 109.81081081081082], [26.0, 119.29545454545452], [27.0, 116.08695652173911], [28.0, 133.1707317073171], [29.0, 123.97368421052634], [30.0, 167.25641025641025], [31.0, 149.1935483870968], [33.0, 115.0], [32.0, 149.25925925925927], [35.0, 132.95], [34.0, 138.43750000000003], [37.0, 188.04166666666666], [36.0, 133.61538461538464], [39.0, 169.1875], [38.0, 215.45833333333334], [41.0, 213.74193548387095], [40.0, 176.78571428571428], [43.0, 213.58695652173918], [42.0, 233.26086956521738], [45.0, 202.67567567567568], [44.0, 196.1176470588235], [47.0, 235.32258064516125], [46.0, 187.92], [48.0, 233.73333333333332], [49.0, 174.42105263157896], [51.0, 306.7], [50.0, 214.75862068965517], [53.0, 193.95000000000005], [52.0, 202.79310344827587], [54.0, 276.625], [55.0, 380.0232558139535], [57.0, 323.0740740740741], [56.0, 236.5454545454545], [59.0, 291.97368421052636], [58.0, 284.8536585365853], [60.0, 254.76666666666662], [61.0, 298.16666666666674], [63.0, 295.09090909090907], [62.0, 290.23809523809524], [64.0, 463.91304347826093], [66.0, 449.7692307692307], [65.0, 349.04166666666663], [67.0, 309.2857142857143], [69.0, 225.0], [68.0, 326.45000000000005], [71.0, 313.3636363636364], [70.0, 198.08333333333337], [72.0, 165.66666666666666], [75.0, 296.375], [74.0, 125.22222222222223], [79.0, 430.5263157894737], [78.0, 330.6470588235294], [77.0, 445.0], [76.0, 78.0], [82.0, 389.1538461538462], [81.0, 452.00000000000006], [80.0, 452.25], [83.0, 443.9285714285715], [86.0, 504.6666666666667], [84.0, 447.375], [85.0, 510.5], [90.0, 662.0], [91.0, 586.0], [89.0, 360.0], [88.0, 504.0]], "isOverall": false, "label": "HTTP Request", "isController": false}, {"data": [[39.53100000000002, 191.03000000000017]], "isOverall": false, "label": "HTTP Request-Aggregated", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 91.0, "title": "Time VS Threads"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: { noColumns: 2,show: true, container: '#legendTimeVsThreads' },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s: At %x.2 active threads, Average response time was %y.2 ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesTimeVsThreads"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotTimesVsThreads"), dataset, options);
            // setup overview
            $.plot($("#overviewTimesVsThreads"), dataset, prepareOverviewOptions(options));
        }
};

// Time vs threads
function refreshTimeVsThreads(){
    var infos = timeVsThreadsInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTimeVsThreads");
        return;
    }
    if(isGraph($("#flotTimesVsThreads"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTimeVsThreads");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTimesVsThreads", "#overviewTimesVsThreads");
        $('#footerTimeVsThreads .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var bytesThroughputOverTimeInfos = {
        data : {"result": {"minY": 13253.033333333333, "minX": 1.72493424E12, "maxY": 22150.0, "series": [{"data": [[1.72493424E12, 13253.033333333333]], "isOverall": false, "label": "Bytes received per second", "isController": false}, {"data": [[1.72493424E12, 22150.0]], "isOverall": false, "label": "Bytes sent per second", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.72493424E12, "title": "Bytes Throughput Over Time"}},
        getOptions : function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity) ,
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Bytes / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendBytesThroughputOverTime'
                },
                selection: {
                    mode: "xy"
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y"
                }
            };
        },
        createGraph : function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesBytesThroughputOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotBytesThroughputOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewBytesThroughputOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Bytes throughput Over Time
function refreshBytesThroughputOverTime(fixTimestamps) {
    var infos = bytesThroughputOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotBytesThroughputOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesBytesThroughputOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotBytesThroughputOverTime", "#overviewBytesThroughputOverTime");
        $('#footerBytesThroughputOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimesOverTimeInfos = {
        data: {"result": {"minY": 191.03000000000017, "minX": 1.72493424E12, "maxY": 191.03000000000017, "series": [{"data": [[1.72493424E12, 191.03000000000017]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.72493424E12, "title": "Response Time Over Time"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average response time was %y ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Times Over Time
function refreshResponseTimeOverTime(fixTimestamps) {
    var infos = responseTimesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotResponseTimesOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesOverTime", "#overviewResponseTimesOverTime");
        $('#footerResponseTimesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var latenciesOverTimeInfos = {
        data: {"result": {"minY": 190.3215000000002, "minX": 1.72493424E12, "maxY": 190.3215000000002, "series": [{"data": [[1.72493424E12, 190.3215000000002]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.72493424E12, "title": "Latencies Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response latencies in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendLatenciesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average latency was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesLatenciesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotLatenciesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewLatenciesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Latencies Over Time
function refreshLatenciesOverTime(fixTimestamps) {
    var infos = latenciesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyLatenciesOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotLatenciesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesLatenciesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotLatenciesOverTime", "#overviewLatenciesOverTime");
        $('#footerLatenciesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var connectTimeOverTimeInfos = {
        data: {"result": {"minY": 0.3105, "minX": 1.72493424E12, "maxY": 0.3105, "series": [{"data": [[1.72493424E12, 0.3105]], "isOverall": false, "label": "HTTP Request", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.72493424E12, "title": "Connect Time Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getConnectTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average Connect Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendConnectTimeOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average connect time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesConnectTimeOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotConnectTimeOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewConnectTimeOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Connect Time Over Time
function refreshConnectTimeOverTime(fixTimestamps) {
    var infos = connectTimeOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyConnectTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotConnectTimeOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesConnectTimeOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotConnectTimeOverTime", "#overviewConnectTimeOverTime");
        $('#footerConnectTimeOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var responseTimePercentilesOverTimeInfos = {
        data: {"result": {"minY": 24.0, "minX": 1.72493424E12, "maxY": 1045.0, "series": [{"data": [[1.72493424E12, 1045.0]], "isOverall": false, "label": "Max", "isController": false}, {"data": [[1.72493424E12, 24.0]], "isOverall": false, "label": "Min", "isController": false}, {"data": [[1.72493424E12, 455.0]], "isOverall": false, "label": "90th percentile", "isController": false}, {"data": [[1.72493424E12, 761.99]], "isOverall": false, "label": "99th percentile", "isController": false}, {"data": [[1.72493424E12, 110.0]], "isOverall": false, "label": "Median", "isController": false}, {"data": [[1.72493424E12, 524.0]], "isOverall": false, "label": "95th percentile", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.72493424E12, "title": "Response Time Percentiles Over Time (successful requests only)"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Response Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentilesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Response time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentilesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimePercentilesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimePercentilesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Time Percentiles Over Time
function refreshResponseTimePercentilesOverTime(fixTimestamps) {
    var infos = responseTimePercentilesOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotResponseTimePercentilesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimePercentilesOverTime", "#overviewResponseTimePercentilesOverTime");
        $('#footerResponseTimePercentilesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var responseTimeVsRequestInfos = {
    data: {"result": {"minY": 57.0, "minX": 97.0, "maxY": 346.0, "series": [{"data": [[139.0, 149.0], [276.0, 84.0], [272.0, 267.5], [153.0, 63.0], [198.0, 57.0], [97.0, 311.0], [197.0, 74.0], [214.0, 346.0], [213.0, 117.0], [241.0, 222.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 276.0, "title": "Response Time Vs Request"}},
    getOptions: function() {
        return {
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Response Time in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: {
                noColumns: 2,
                show: true,
                container: '#legendResponseTimeVsRequest'
            },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median response time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesResponseTimeVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotResponseTimeVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewResponseTimeVsRequest"), dataset, prepareOverviewOptions(options));

    }
};

// Response Time vs Request
function refreshResponseTimeVsRequest() {
    var infos = responseTimeVsRequestInfos;
    prepareSeries(infos.data);
    if (isGraph($("#flotResponseTimeVsRequest"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeVsRequest");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimeVsRequest", "#overviewResponseTimeVsRequest");
        $('#footerResponseRimeVsRequest .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var latenciesVsRequestInfos = {
    data: {"result": {"minY": 57.0, "minX": 97.0, "maxY": 345.5, "series": [{"data": [[139.0, 136.0], [276.0, 84.0], [272.0, 267.5], [153.0, 63.0], [198.0, 57.0], [97.0, 310.0], [197.0, 74.0], [214.0, 345.5], [213.0, 115.0], [241.0, 222.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 276.0, "title": "Latencies Vs Request"}},
    getOptions: function() {
        return{
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Latency in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: { noColumns: 2,show: true, container: '#legendLatencyVsRequest' },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median Latency time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesLatencyVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotLatenciesVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewLatenciesVsRequest"), dataset, prepareOverviewOptions(options));
    }
};

// Latencies vs Request
function refreshLatenciesVsRequest() {
        var infos = latenciesVsRequestInfos;
        prepareSeries(infos.data);
        if(isGraph($("#flotLatenciesVsRequest"))){
            infos.createGraph();
        }else{
            var choiceContainer = $("#choicesLatencyVsRequest");
            createLegend(choiceContainer, infos);
            infos.createGraph();
            setGraphZoomable("#flotLatenciesVsRequest", "#overviewLatenciesVsRequest");
            $('#footerLatenciesVsRequest .legendColorBox > div').each(function(i){
                $(this).clone().prependTo(choiceContainer.find("li").eq(i));
            });
        }
};

var hitsPerSecondInfos = {
        data: {"result": {"minY": 33.333333333333336, "minX": 1.72493424E12, "maxY": 33.333333333333336, "series": [{"data": [[1.72493424E12, 33.333333333333336]], "isOverall": false, "label": "hitsPerSecond", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.72493424E12, "title": "Hits Per Second"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of hits / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendHitsPerSecond"
                },
                selection: {
                    mode : 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y.2 hits/sec"
                }
            };
        },
        createGraph: function createGraph() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesHitsPerSecond"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotHitsPerSecond"), dataset, options);
            // setup overview
            $.plot($("#overviewHitsPerSecond"), dataset, prepareOverviewOptions(options));
        }
};

// Hits per second
function refreshHitsPerSecond(fixTimestamps) {
    var infos = hitsPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if (isGraph($("#flotHitsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesHitsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotHitsPerSecond", "#overviewHitsPerSecond");
        $('#footerHitsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var codesPerSecondInfos = {
        data: {"result": {"minY": 16.666666666666668, "minX": 1.72493424E12, "maxY": 16.666666666666668, "series": [{"data": [[1.72493424E12, 16.666666666666668]], "isOverall": false, "label": "200", "isController": false}, {"data": [[1.72493424E12, 16.666666666666668]], "isOverall": false, "label": "202", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.72493424E12, "title": "Codes Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendCodesPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "Number of Response Codes %s at %x was %y.2 responses / sec"
                }
            };
        },
    createGraph: function() {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesCodesPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotCodesPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewCodesPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Codes per second
function refreshCodesPerSecond(fixTimestamps) {
    var infos = codesPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotCodesPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesCodesPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotCodesPerSecond", "#overviewCodesPerSecond");
        $('#footerCodesPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var transactionsPerSecondInfos = {
        data: {"result": {"minY": 33.333333333333336, "minX": 1.72493424E12, "maxY": 33.333333333333336, "series": [{"data": [[1.72493424E12, 33.333333333333336]], "isOverall": false, "label": "HTTP Request-success", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.72493424E12, "title": "Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTransactionsPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                }
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTransactionsPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTransactionsPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewTransactionsPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Transactions per second
function refreshTransactionsPerSecond(fixTimestamps) {
    var infos = transactionsPerSecondInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTransactionsPerSecond");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotTransactionsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTransactionsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTransactionsPerSecond", "#overviewTransactionsPerSecond");
        $('#footerTransactionsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var totalTPSInfos = {
        data: {"result": {"minY": 33.333333333333336, "minX": 1.72493424E12, "maxY": 33.333333333333336, "series": [{"data": [[1.72493424E12, 33.333333333333336]], "isOverall": false, "label": "Transaction-success", "isController": false}, {"data": [], "isOverall": false, "label": "Transaction-failure", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.72493424E12, "title": "Total Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTotalTPS"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                },
                colors: ["#9ACD32", "#FF6347"]
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTotalTPS"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTotalTPS"), dataset, options);
        // setup overview
        $.plot($("#overviewTotalTPS"), dataset, prepareOverviewOptions(options));
    }
};

// Total Transactions per second
function refreshTotalTPS(fixTimestamps) {
    var infos = totalTPSInfos;
    // We want to ignore seriesFilter
    prepareSeries(infos.data, false, true);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 32400000);
    }
    if(isGraph($("#flotTotalTPS"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTotalTPS");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTotalTPS", "#overviewTotalTPS");
        $('#footerTotalTPS .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

// Collapse the graph matching the specified DOM element depending the collapsed
// status
function collapse(elem, collapsed){
    if(collapsed){
        $(elem).parent().find(".fa-chevron-up").removeClass("fa-chevron-up").addClass("fa-chevron-down");
    } else {
        $(elem).parent().find(".fa-chevron-down").removeClass("fa-chevron-down").addClass("fa-chevron-up");
        if (elem.id == "bodyBytesThroughputOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshBytesThroughputOverTime(true);
            }
            document.location.href="#bytesThroughputOverTime";
        } else if (elem.id == "bodyLatenciesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesOverTime(true);
            }
            document.location.href="#latenciesOverTime";
        } else if (elem.id == "bodyCustomGraph") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCustomGraph(true);
            }
            document.location.href="#responseCustomGraph";
        } else if (elem.id == "bodyConnectTimeOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshConnectTimeOverTime(true);
            }
            document.location.href="#connectTimeOverTime";
        } else if (elem.id == "bodyResponseTimePercentilesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimePercentilesOverTime(true);
            }
            document.location.href="#responseTimePercentilesOverTime";
        } else if (elem.id == "bodyResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeDistribution();
            }
            document.location.href="#responseTimeDistribution" ;
        } else if (elem.id == "bodySyntheticResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshSyntheticResponseTimeDistribution();
            }
            document.location.href="#syntheticResponseTimeDistribution" ;
        } else if (elem.id == "bodyActiveThreadsOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshActiveThreadsOverTime(true);
            }
            document.location.href="#activeThreadsOverTime";
        } else if (elem.id == "bodyTimeVsThreads") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTimeVsThreads();
            }
            document.location.href="#timeVsThreads" ;
        } else if (elem.id == "bodyCodesPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCodesPerSecond(true);
            }
            document.location.href="#codesPerSecond";
        } else if (elem.id == "bodyTransactionsPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTransactionsPerSecond(true);
            }
            document.location.href="#transactionsPerSecond";
        } else if (elem.id == "bodyTotalTPS") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTotalTPS(true);
            }
            document.location.href="#totalTPS";
        } else if (elem.id == "bodyResponseTimeVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeVsRequest();
            }
            document.location.href="#responseTimeVsRequest";
        } else if (elem.id == "bodyLatenciesVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesVsRequest();
            }
            document.location.href="#latencyVsRequest";
        }
    }
}

/*
 * Activates or deactivates all series of the specified graph (represented by id parameter)
 * depending on checked argument.
 */
function toggleAll(id, checked){
    var placeholder = document.getElementById(id);

    var cases = $(placeholder).find(':checkbox');
    cases.prop('checked', checked);
    $(cases).parent().children().children().toggleClass("legend-disabled", !checked);

    var choiceContainer;
    if ( id == "choicesBytesThroughputOverTime"){
        choiceContainer = $("#choicesBytesThroughputOverTime");
        refreshBytesThroughputOverTime(false);
    } else if(id == "choicesResponseTimesOverTime"){
        choiceContainer = $("#choicesResponseTimesOverTime");
        refreshResponseTimeOverTime(false);
    }else if(id == "choicesResponseCustomGraph"){
        choiceContainer = $("#choicesResponseCustomGraph");
        refreshCustomGraph(false);
    } else if ( id == "choicesLatenciesOverTime"){
        choiceContainer = $("#choicesLatenciesOverTime");
        refreshLatenciesOverTime(false);
    } else if ( id == "choicesConnectTimeOverTime"){
        choiceContainer = $("#choicesConnectTimeOverTime");
        refreshConnectTimeOverTime(false);
    } else if ( id == "choicesResponseTimePercentilesOverTime"){
        choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        refreshResponseTimePercentilesOverTime(false);
    } else if ( id == "choicesResponseTimePercentiles"){
        choiceContainer = $("#choicesResponseTimePercentiles");
        refreshResponseTimePercentiles();
    } else if(id == "choicesActiveThreadsOverTime"){
        choiceContainer = $("#choicesActiveThreadsOverTime");
        refreshActiveThreadsOverTime(false);
    } else if ( id == "choicesTimeVsThreads"){
        choiceContainer = $("#choicesTimeVsThreads");
        refreshTimeVsThreads();
    } else if ( id == "choicesSyntheticResponseTimeDistribution"){
        choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        refreshSyntheticResponseTimeDistribution();
    } else if ( id == "choicesResponseTimeDistribution"){
        choiceContainer = $("#choicesResponseTimeDistribution");
        refreshResponseTimeDistribution();
    } else if ( id == "choicesHitsPerSecond"){
        choiceContainer = $("#choicesHitsPerSecond");
        refreshHitsPerSecond(false);
    } else if(id == "choicesCodesPerSecond"){
        choiceContainer = $("#choicesCodesPerSecond");
        refreshCodesPerSecond(false);
    } else if ( id == "choicesTransactionsPerSecond"){
        choiceContainer = $("#choicesTransactionsPerSecond");
        refreshTransactionsPerSecond(false);
    } else if ( id == "choicesTotalTPS"){
        choiceContainer = $("#choicesTotalTPS");
        refreshTotalTPS(false);
    } else if ( id == "choicesResponseTimeVsRequest"){
        choiceContainer = $("#choicesResponseTimeVsRequest");
        refreshResponseTimeVsRequest();
    } else if ( id == "choicesLatencyVsRequest"){
        choiceContainer = $("#choicesLatencyVsRequest");
        refreshLatenciesVsRequest();
    }
    var color = checked ? "black" : "#818181";
    if(choiceContainer != null) {
        choiceContainer.find("label").each(function(){
            this.style.color = color;
        });
    }
}

