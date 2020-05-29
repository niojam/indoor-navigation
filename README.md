# Indoor navigation

## Flow 

For better understanding of my logic, I decided to describe the flow.  

#### Detection

1) Base Station detects mobile station and sends post request to RestEndpoint1
 ``report/{baseStationId}`` with List of detected  MobileStationDtos in request body. 
 (see java/ee/wemakesoftware/navigation/api/BaseStationController.java)
2) Report is saved to db and checked if BaseStation with that id is registered or not.
If not exception is thrown and response is in user friendly format (see java/ee/wemakesoftware/navigation/error)
3) DetectionService checks if mobile station is in db and adds if does not. To improve locating algorithm and reduce memory usage
there is stored only one BaseStation detection of Mobile Station which was the latest. When Mobile Station is saved to db
x and y coordinates are null, because we had no necessity to calculate them.
(see java/ee/wemakesoftware/navigation/service/DetectionService.java) 

#### Locating

1) User wants to get location of mobile station. He sends get request on RestEndpoint2
``/location/{id}``. (see java/ee/wemakesoftware/navigation/api/MobileStationController.java)
2) MsLocationService takes 3 latest detection reports and calculates Mobile Station location using
Trilateration algorithm. That means we need at least 3 detections to calculate Mobile Station coordinates.
If there are not enough records or Mobile Station id is not in DB custom exception is thrown. User can see
userfriendly output. (see java/ee/wemakesoftware/navigation/error in case of ecxeption and 
java/ee/wemakesoftware/navigation/pojo/MobileStationPositionDto.java everything is ok)
3) New coordinates are saved on database.