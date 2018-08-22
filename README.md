# ImgurGSearch

ImgurGSearch Application is designed for Searching images from Imgur Gallery based on tags entered by user.
Along with tag based searching,a toggle is provided to user for filtering search results based on Even sum of "points","score" & "topic_id"
from the Json result obtained from server against an image.

Each Image display the following for each search result:
title
date of post in local time (DD/MM/YYYY h:mm a)
number of additional images in post (if there are multiple)
image
If a search result contains multiple images, it displays the first image from it's list.

Imgur Gallery URL used for searching top images of the week:
https://api.imgur.com/3/gallery/top/week/search?q=tag

This Application is using MVC pattern for displaying results.

Steps to use app:
1. Gradle build application after cloning it in android studio.
2. Insatll generated apk in device.
3. open Application
4. tap the search icon on the menu bar
5. Enter the tag for searching the images from Imgur Gallery
6. Press search icon on soft keyboard
7. Press back to remove soft keyboard
8. Search results are shown
9. use Even points sum toggle to filter the results based on even sum.


Thanks for using this application
