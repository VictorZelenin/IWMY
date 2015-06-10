# IWMY Speed Dating Backend API

This is the Google App Engine backend which provides API
for [IWMY Speed Dating](../README.md) Android app (and other apps which can be developed).


## API Documentation

***Note***. If you are a developer,
it's highly recommended to run the local (Developer) version of the backend for debugging your app.
You can read about it [here](../README.md).

The API methods operate with (receive and return) entities of several **object types**.
The API consists of the **General** and **Special** methods.


### Object types

There are the several types of entities used in IWMY Speed Dating data model,
they are received and returned by API methods as JSON objects.
It's recommended to use JSON libraries for managing JSON objects in apps being developed.

#### User

The user account. Users can be "normal users" who attend various events,
and organizers of these events.

Here is the description of data fields of User ( ***note***: no description means
the field is for use in the backend, or for purposes not yet implemented, and it
must be not changed by the app in the existing objects and must be set empty in the new objects):

| Field | Type | Description |
| ----- | ---- | ----------- |
| _userId | String |  |
| affair | String |  |
| attitudeToAlcohol | String |  |
| attitudeToSmoking | String |  |
| birthDate | String | Birth date in format `yyyy-MM-dd`, for example, `1970-01-01` |
| city | String | City the user lives in |
| country | String | Country the user lives in |
| creationTime | String |  |
| email | String | User email. Is unique for users. Must contain `@`. Must not contain `&#124;` |
| gender | String | User gender, `male` or `female` |
| goal | String |  |
| group | String | `user` for "normal user", `organizer` for events organizer who is activated by admin, `pendingOrganizer` for events organizer who registered but is not yet activated by admin |
| height | String |  |
| isChecked | String | Determines if the user is checked (selected) in the app, `true` or `false`. Has no effect on the backend |
| location | String |  |
| nameAndSurname | String | User name and surname |
| organization | String | The name of organization which the organizer represents. Empty for "normal users" |
| orientation | String |  |
| password | String | The password. Empty for users created by the organizer, non-empty for any other users |
| phone | String | Phone number |
| photo | String | User photo ID |
| referralEmail | String | Organizer email for users created by that organizer; must contain `@`; must not contain `&#124;`. For users not created by organizer it's empty |
| thumbnail | String | User photo thumbnail (preview) ID |
| username | String | User's username. Must be not empty. Is unique for users |
| website | String | The website of organization which the organizer represents. Empty for "normal users" |
| weight | String |  |

***Note.*** Order of fields is alphabetical for this and following JSON object types.

Example of an array of 1 User object (formatted for readability):

    [
        {
            "_userId":"",
            "affair":"",
            "attitudeToAlcohol":"",
            "attitudeToSmoking":"",
            "birthDate":"1970-01-01",
            "city":"Dallas",
            "country":"USA",
            "creationTime":"",
            "email":"susan@example.com",
            "gender":"female",
            "goal":"",
            "group":"user",
            "height":"",
            "isChecked":"false",
            "location":"",
            "nameAndSurname":"Susan",
            "organization":"",
            "orientation":"",
            "password":"qwerty",
            "phone":"1234567890",
            "photo":"_42363",
            "referralEmail":"",
            "thumbnail":"_31328",
            "username":"susan",
            "website":"",
            "weight":""
        }
    ]


#### Event

The event created by an organizer and attended by "normal users".

Description of data fields:

| Field | Type | Description |
| ----- | ---- | ----------- |
| _eventId | String |  |
| actual | String | Shows if this event is not yet actually held (`true`, the default value) or it is already (`false`) |
| allowSendingRatings | String | Defines if the organizer of this event allows (`true`) or does not allow (`false`, the default value) "normal users" to start giving votes (ratings) at this event |
| city | String | The city where the event is held. |
| cost | String | Event cost |
| country | String | The country where the event is held. |
| description | String | The textual description of the event |
| freePlaces | String | The number of attenders which are supposed to attend the event |
| maxAllowedAge | String | Maximal age of "normal users" who can see and attend the event. An integer number. Empty if no limitation |
| maxRatingsPerUser | String | Maximal quantity of other "normal users" to whom a "normal user" can give votes (positive ratings) at the event. An integer number. The default value is 0 |
| minAllowedAge | String | Minimal age of "normal users" who can see and attend the event. An integer number. Empty if no limitation |
| organizerEmail | String | Email addredd of the organizer who created the event |
| photo | String | Event photo or graphics ID |
| place | String | The name of the place where the event is held |
| streetAddress | String | Street address where the event is held |
| thumbnail | String | Event photo or graphics thumbnail (preview) ID |
| time | String | Event start time in the format `yyyy-MM-dd HH:mm`, for example, `2015-05-27 18:30` |

Example of an array of 1 Event object (formatted for readability):

    [
        {
            "_eventId":"",
            "actual":"true",
            "allowSendingRatings":"false",
            "city":"New York",
            "cost":"49",
            "country":"USA",
            "description":"A cool event",
            "freePlaces":"22",
            "maxAllowedAge":"",
            "maxRatingsPerUser":"4",
            "minAllowedAge":"18",
            "organizerEmail":"joe@example.com",
            "photo":"_87476",
            "place":"Washington Square Arch",
            "streetAddress":"Fifth Avenue",
            "thumbnail":"",
            "time":"2015-05-27 18:30"
        }
    ]


#### Attendance

The record about a user attending an event.

Description of data fields:

| Field | Type | Description |
| ----- | ---- | ----------- |
| _attendanceId | String |  |
| active | String | Defines if the organizer of the event selected (`true`) the attending "normal user" of this event for giving votes (ratings) at this event, or not (`false`, the default value) |
| creationTime | String |  |
| eventOrganizerEmail | String | The email address of the organizer who created the event |
| eventTime | String | Event start time in the format `yyyy-MM-dd HH:mm`, for example, `2015-05-27 18:30` |
| userEmail | String | The email address of the "normal user" who attends the event |
| userGender | String | Gender of the "normal user" who attends the event, `female` or `male` |
| username | String | The username of the "normal user" who attends the event |

Example of an array of 1 Attendance object (formatted for readability):

    [
        {
            "_attendanceId":"",
            "active":"false",
            "creationTime":"",
            "eventOrganizerEmail":"joe@example.com",
            "eventTime":"2015-05-27 18:30",
            "userEmail":"susan@example.com",
            "userGender":"female",
            "username":"susan"
        }
    ]


#### Couple

The record about a pair of 2 users.

Description of data fields:

| Field | Type | Description |
| ----- | ---- | ----------- |
| _coupleId | String |  |
| birthDate1 | String | Birth date of the user 1 of the couple, in format `yyyy-MM-dd`, for example, `1970-01-01` |
| birthDate2 | String | Birth date of the user 2 of the couple, in format `yyyy-MM-dd`, for example, `1970-01-01` |
| eventOrganizerEmail | String | The email of the organizer who created the event where the couple was formed |
| eventTime | String | The tart time of the event where the couple was formed, in the format `yyyy-MM-dd HH:mm`, for example, `2015-05-27 18:30` |
| name1 | String | The name and surname of the user 1 of the couple |
| name2 | String | The name and surname of the user 2 of the couple |
| phone1 | String | The phone number of the user 1 of the couple |
| phone2 | String | The phone number of the user 2 of the couple |
| thumbnail1 | String | User 1 photo thumbnail (preview) ID |
| thumbnail2 | String | User 2 photo thumbnail (preview) ID |
| userEmail1 | String | The email address of the user 1 of the couple |
| userEmail2 | String | The email address of the user 2 of the couple |
| username1 | String | The username of the user 1 of the couple |
| username2 | String | The username of the user 2 of the couple |

Example of an array of 1 Couple object (formatted for readability):

    [
        {
            "_coupleId":"",
            "birthDate1":"1981-04-12",
            "birthDate2":"1982-06-15",
            "eventOrganizerEmail":"joe@example.com",
            "eventTime":"2015-05-27 18:30",
            "name1":"Martin",
            "name2":"Susan",
            "phone1":"0987654321",
            "phone2":"1234567890",
            "thumbnail1":"_23423",
            "thumbnail2":"_34234",
            "userEmail1":"martin@example.com",
            "userEmail2":"susan@example.com",
            "username1":"martin",
            "username2":"susan"
        }
    ]


#### Rating

The vote of a user for another user.

Description of data fields:

| Field | Type | Description |
| ----- | ---- | ----------- |
| _ratingId | String |  |
| actual | String | Defines if the voting (rating) is submitted (`true`) or still is supposed to be edited (`false`, the default value) |
| comment | String | A note which the "normal user" temporarily, privately creates about the other "normal user" |
| eventOrganizerEmail | String | The email address of the organizer who created the event where the voting (ratings) is happening |
| eventTime | String | Event (where the voting (ratings) is happening) start time in the format `yyyy-MM-dd HH:mm`, for example, `2015-05-27 18:30` |
| number | String | The number of the rating in the list. An integer number |
| otherUserEmail | String | The email address of the "normal user" to whom this "normal user" gives the vote (rating) |
| selection | String | Defines if the "normal user" actually gave the vote (rating) to the other "normal user" (`selected`) or not (empty, default value) |
| thisUserEmail | String | The email address of the "normal user" who gives the vote (rating) to the other "normal user" |
| username | String | The username of the "normal user" who gives the vote (rating) to the other "normal user" |

Example of an array of 1 Rating object (formatted for readability):

    [
        {
            "_ratingId":"",
            "actual":"true",
            "comment":"A nice person",
            "eventOrganizerEmail":"joe@example.com",
            "eventTime":"2015-05-27 18:30",
            "number":"3",
            "otherUserEmail":"martin@example.com",
            "selection":"selected",
            "thisUserEmail":"susan@example.com",
            "username":"susan"
        }
    ]


#### Email

The record about an email sent or not sent from/to a user or the backend.

Description of data fields:

| Field | Type | Description |
| ----- | ---- | ----------- |
| _emailId | String |  |
| fromAddress | String | The sender's email address |
| fromName | String | The email sender's name |
| message | String | The text of the email supposed to be sent |
| subject | String | The subject of the email supposed to be sent |
| toAddress | String | The receiver's email address |
| toName | String | The email receiver's name |

Example of an array of 1 Email object (formatted for readability):

    [
        {
            "_emailId":"",
            "fromAddress":"app@example.com",
            "fromName":"The App",
            "message":"The Message",
            "subject":"The Subject",
            "toAddress":"susan@example.com",
            "toName":"Susan"
        }
    ]


### General API methods

The General API methods are the majority of API methods required for application development.

They have the following **characteristics**:

1. They are HTTP POST methods.

2. They have fixed URLs, specific for each method.
The URL consists of the host name and the method path.
For example, the URL `https://example.com/events/get/count`
has hostname `example.com` and path `/events/get/count`.
***Note 1***. In this documentation, for each method the path is provided
not including the hostname and including the leading slash.
For example, the URL `https://example.com/events/get/count` has path `/events/get/count`.
***Note 2***. When you are running a Developer version of the backend in the same network
where the app is being developed and the IP address of the computer where the backend is running
is, for example, `192.168.1.10`, your hostname is `192.168.1.10:8082`, and insecure HTTP is used.
The default port `8082` is used. The example API method's URL for the path `/events/get/count`
from above will be `http://192.168.1.10:8082/events/get/count`.
The production backend hostname is `iwmy-speed-dating.appspot.com`. HTTPS is used.
Therefore the example API method's URL for the path `/events/get/count` from above
will be `https://iwmy-speed-dating.appspot.com/events/get/count`.

2. They consume and produce media type `application/json;charset=utf-8`
(thus they require the HTTP header `Content-Type: application/json;charset=utf-8`).

3. They consume and produce JSON arrays of objects in the bodies of the HTTP requests and responses.
All JSON objects of the request or the response are of the same type.
The types of JSON objects are specified for each API method,
for the request and response, individually.

4. Depending on security requirements, they do or do not
(this is specified for every method) require Basic Authorization.
In other words, they may require the HTTP header of type `Authorization: Basic base64`,
where `base64` is the line `email:password` encoded in Base64.
For example, for the app user logged in with the account
with email address `joe@example.com` and password `qwerty`,
the Base64-encoded line `joe@example.com:qwerty` will be `am9lQGV4YW1wbGUuY29tOnF3ZXJ0eQ==`,
and the entire Basic Authorization header
will be `Authorization: Basic am9lQGV4YW1wbGUuY29tOnF3ZXJ0eQ==`.
If Basic Authorization is required for the particular method,
but no such user account is registered in the backend database or the password is wrong,
the HTTP status 401 (Unauthorized) will be returned.
If you are developing an application and do not have an account as a user, please register one
using the Developer version of the Android app (building instructions are [here](../README.md))
or the API methods for registering a new user or organizer.
***Note***. The production backend works over HTTPS only.

5. The API method calls can include the HTTP header `Content-Version: version`
where `version` is the version of your application, including its name and platform information.
For example, for an iOS app it can be `Content-Version: IWMY Speed Dating 2.1.4 for iOS`.
It's recommended to include this header in your API calls permanently for debugging purposes.

The list of General method paths:

#### /users/get/for/event/active/reset

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | User | Yes |

For the event defined by the values of fields `organizerEmail` and `time` (other fields are ignored)
of the single Event object passed to this method, the method does the following actions:

1. Deletes all Rating and Couple entities which could be previously created at this event.

2. Sets the `maxRatingsPerUser` property of the event to `0`.
Apps must not allow users to give votes (ratings) to other users at the event
if the `maxRatingsPerUser` property of the event is `0`.

3. Sets the `allowSendingRatings` property of the event to `false`.
Apps must not allow users who give votes (ratings) to other users at the event to submit them
if the `allowSendingRatings` property of the event is not `true`.

4. The "normal users" who attend the event are set to be not selected by the event organizer
for giving votes (ratings) for other "normal users".

5. Returns the "normal users" who attend the event.

The users who attend the event are returned in the array of User objects.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /users/remove/attendance

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | User | Yes |

Removes a "normal user" from an event by removing his attendance.
The event and the attending user are defined by the values
of fields `eventOrganizerEmail`, `eventTime` and `userEmail` of the single Attendance object.
Other fields of the Attendance object are ignored.

The users who remain attending the event are returned in the array of User objects.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /users/get/for/event/active

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | User | Yes |

Gets the "normal users" who attend the event and are selected by the event organizer
for giving votes (ratings) for other "normal users".
The event is defined by the values of fields `organizerEmail` and `time` of the single Event object.
Other fields of the Event object are ignored.

The users who attend the event and are selected by the event organizer
for giving votes (ratings) for other "normal users" are returned in the array of User objects.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /users/get/for/event

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | User | Yes |

Gets the "normal users" who attend the event defined by
the values of fields `organizerEmail` and `time` of the single Event object.
Other fields of the Event object are ignored.

The users who attend the event are returned in the array of User objects.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /users/get/for/event/lock

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | User | Yes |

Prevents the "normal users" who attend the event defined by
the values of fields `organizerEmail` and `time` of the single Event object
from giving and submitting votes (ratings).
Other fields of the Event object are ignored.

Sets `maxRatingsPerUser` property of the event to `0` and `allowSendingRatings` property to `false`.

Apps must not allow users to give votes (ratings) to other users at the event
if the `maxRatingsPerUser` property of the event is `0`.

Apps must not allow users who give votes (ratings) to other users at the event to submit them
if the `allowSendingRatings` property of the event is not `true`.

The users who attend the event are returned in the array of User objects.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /users/get

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | Yes |

Gets the "normal user" whose email address equals the value of field `email`
of the single User object passed to the method.
Other fields of the User object passed to the method are ignored.

Produces an array of 1 User object if such user exists in the backend database,
or an empty array otherwise.

Sensitive information (the `password` field value) may be empty in the User object
returned in the array by the method.

#### /users/get/login

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | No |

Gets the "normal user" whose email address equals the value of field `email`
and/or username equals the value of field `username`,
and password equals the value of field `password`
of the single User object passed to the method.
Other fields of the User object passed to the method are ignored.

The data of the User object returned by the method should be stored in the app as user account data.
The Basic Authorization header is formed from the account data as well.

Produces an array of 1 User object if such user exists in the backend database,
and the password is correct, or an empty array otherwise.

#### /users/add

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | No |

Adds the "normal user" (`group` field value is forced to `user`)
whose data is passed to the method in a single User object to the backend database.
It's called when the "normal user" signs up.

The User object passed to the method can have a user photo attached.
In this case the `thumbnail` and `photo` field values must be not empty,
but Base64-encoded binary data of the JPEG photo preview (thumbnail) and JPEG photo, respectively.
The thumbnail binary data size must be less than 5 kB (1 kB = 1000 bytes).
The photo binary data size must be less than 500 kB.

The value of the field `password` must be not empty.

Produces an array of 1 User object if the user was successfully added,
or an empty array if the user with this username or email address already exists
in the backend database.

If the user with photo is added, the `photo` and `thumbnail` fields in the User object
in the response contain photo and thumbnail IDs instead of Base64 data, respectively.

#### /users/add/pending/organizer

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | No |

Adds the not yet activated organizer (`group` field value is forced to `pendingOrganizer`)
whose data is passed to the method in a single User object to the backend database.
It's called when the organizer signs up.

After signing up, the organizer cannot log in until he is activated manually by the admin.
This is achieved by temporarily appending `_token` to the organizer's password,
where `token` is a randomly generated secret number.
The admin activates the pending organizer
by calling the Special method `/users/activate/pending/organizer/token`
which recovers the original password of the organizer (he can then log in)
and changes his `group` from `pendingOrganizer` to `organizer`.

The value of the field `password` must be not empty.

Produces an array of 1 User object if the not yet activated organizer was successfully added,
or an empty array if the user with this username or email address already exists
in the backend database.

#### /users/add/by/organizer

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | Yes |

Adds the "normal user" (`group` field value is forced to `user`)
whose data is passed to the method in a single User object to the backend database.
It's called when the "normal user" is being registered by the organizer.

The User object passed to the method can have a user photo attached.
In this case the `thumbnail` and `photo` field values must be not empty,
but Base64-encoded binary data of the JPEG photo preview (thumbnail) and JPEG photo, respectively.
The thumbnail binary data size must be less than 5 kB (1 kB = 1000 bytes).
The photo binary data size must be less than 500 kB.

The value of the field `referralEmail` must be the email address of the organizer
who registers the "normal user".

The value of the field `password` must be empty.

Produces an array of 1 User object if the user was successfully added,
or an empty array if the user with this username or email address already exists
in the backend database.

If the user with photo is added, the `photo` and `thumbnail` fields in the User object
in the response contain photo and thumbnail IDs instead of Base64 data, respectively.

#### /users/replace

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | User | Yes |

Consumes the array of 2 User objects: the first one contains the current username and email as
`username` and `email` field values respectively (other fields are ignored),
and the second one contains the full new data for the user to be put to the backend database
instead of old data.

This method is called when the user account data is being changed in the app.
A "normal user" can change his own account data
and an organizer can change the account data of a user whom he added personally.

The second User object passed to the method can have a user photo attached.
In this case the `thumbnail` and `photo` field values must be not empty,
but Base64-encoded binary data of the JPEG photo preview (thumbnail) and JPEG photo, respectively.
The thumbnail binary data size must be less than 5 kB (1 kB = 1000 bytes).
The photo binary data size must be less than 500 kB.

Produces an array of 1 User object which is stored in the backend database after replacement
(if the new user data has either new `username` and `email` field values not yet used,
or the same ones),
or an empty array (if such username or email were already taken by other user before).

The value of the field `password` of the second User object passed to the method must be not empty.

If the user photo is replaced with the new one, the `photo` and `thumbnail` fields in the User
object in the response contain new photo and thumbnail IDs instead of Base64 data, respectively.

#### /users/get/other/for/event

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | User | Yes |

Gets all "normal users" who are added by all organizers and are not attending the event defined by
the values of fields `organizerEmail` and `time` of the single Event object passed to the method.
Other fields of the Event object are ignored.

This method is called to get the lists of existing "normal users"
whom the organizer of the event can set to attend the event.

Sensitive information (the `password` field value) may be empty in each User object
returned in the array by the method.

#### /events/get

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Gets all events created by the organizer whose email address equals the value
of the field `organizerEmail` of the single Event object passed to the method.
Other fields of the Event object passed to the method are ignored.

#### /events/get/for/time

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Gets the event created by the organizer whose email address equals the value
of the field `organizerEmail` and having start time which equals the value of the field `time`
of the single Event object passed to the method.
Other fields of the Event object passed to the method are ignored.

Produces an array of 1 Event object if such event exists in the backend database,
or an empty array otherwise.

#### /events/get/for/attendance/active

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Event | Yes |

Gets all events attended by a user, Attendance record's
`eventOrganizerEmail` field value of which equals the `organizerEmail` field value of each event
returned by the method,
`eventTime` field value of which equals the `time` field value of each event returned by the method,
and `active` field value of which equals `true`
which means that the "normal users" who attend the event are selected by the event organizer
for giving votes (ratings) for other "normal users".

Only those events are included which have the `maxRatingsPerUser` property set to `0`.
It means that those events are not yet started by the organizer,
and apps must not allow users to give votes (ratings) to other users at the event.

If the attendance record doesn't apply to any user existing in the backend database
attending the existing event, or there are no such an event which the user attends
and the organizer of which selected him for giving votes (ratings) for other users,
an empty list is returned.

#### /events/get/for/user

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | Event | Yes |

Gets events attended by the user defined in the single User object sent to the method,
and more precisely, by the `email` field value (other fields are ignored).

If the user doesn't exist in the backend database or doesn't attend eny existing events,
an empty array is returned.

#### /events/get/all/for/user

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| User | Event | Yes |

Gets events attended by or available for attending by the user defined in the single User object
sent to the method, and more precisely, by the `email` field value (other fields are ignored).

The user's age (an integer number) is calculated by the backend from his birth date.
If the user's age does not fall into the range between the `minAllowedAge` and `maxAllowedAge`
field values of an event, that event is not included into the list to return by the method.
The empty `minAllowedAge` and `maxAllowedAge` field value means 0 and infinity, respectively.

If an event is already held (its `actual` field value is `false`),
that event is not included into the list to return by the method.

If the user doesn't exist in the backend database or doesn't attend eny existing events
and no actual and age-fitting existing events are available for him, an empty array is returned.

#### /events/add

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Adds the event, data of which is passed to the method in a single Event object,
to the backend database. It's called when an organizer creates the event.

The Event object passed to the method can have an event photo (graphics) attached.
In this case the `thumbnail` and `photo` field values must be not empty,
but Base64-encoded binary data of the JPEG photo preview (thumbnail) and JPEG photo, respectively.
The thumbnail binary data size must be less than 5 kB (1 kB = 1000 bytes).
The photo binary data size must be less than 500 kB.

Produces an array of 1 Event object if the event was successfully added,
or an empty array if the actual event with such `organizerEmail` or `time` or `place` properties
already exists, or if the non-actual event with such `organizerEmail` or `time` properties
already exists in the backend database.
(The actual event, with the `actual` property value `false`,
is that one which was not yet actually held).

If the event with photo (graphics) is added, the `photo` and `thumbnail` fields in the Event object
in the response contain photo and thumbnail IDs instead of Base64 data, respectively.

#### /events/put

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Saves the Event object passed to the method to the backend database.
Used for overwriting some properties of the existing event.
The event must already exist in the backend database.
(The unique events are destinguished by `organizerEmail` and `time` properties.)

Produces an array of 1 Event object, the same which was passed to the method.

#### /events/delete

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Removes the event, defined by field values `organizerEmail` and `time`
of the Event object passed to the method, to the backend database.
Other fields on the Event object passed to the method are ignored

Produces an empty array if the event was deleted,
or the array of 1 Event object, the same which was passed to the method,
if no such event existed in the backend database.

#### /events/set/unactual

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Sets the event already held.
In other words, for the event, defined by field values `organizerEmail` and `time`
of the Event object passed to the method, the property `actual` is set to `false`.
Other fields on the Event object passed to the method are ignored.

This method is called when the previously "new" event was actually held
and is marked as already held (and not anymore available for attending and holding).
It's done when the couples are created in the event.

Produces the array of 1 Event object which already has the `actual` field value `false`,
or, if no such event existed in the backend database, an empty array.

#### /events/set/user/ratings/allow

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Sets the event, for attending users who are selected by the event organizer
for giving votes (ratings) for other users, available for actually submitting the votes (ratings).
In other words, it sets the `allowSendingRatings` property of the event to `true`.
The event is defined by by field values `organizerEmail` and `time` of the Event object
passed to the method, other fields are ignored.

This method is called when the event organizer finishes giving votes (ratings) for other users
from behalf of the users whose accounts he created,
and allows other users to submit their votes (ratings).

Produces the array of 1 Event object which already has the `actual` field value `false`,
or, if no such event existed in the backend database, an empty array.

#### /events/replace

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Event | Yes |

Consumes the array of 2 Event objects: the first one contains the current organizer email and time
as `organizerEmail` and `time` field values respectively (other fields are ignored),
and the second one contains the full new data for the event to be put to the backend database
instead of old data.

This method is called when the organizer changes his event's data in the app.

The second Event object passed to the method can have a photo (graphics) attached.
In this case the `thumbnail` and `photo` field values must be not empty,
but Base64-encoded binary data of the JPEG photo preview (thumbnail) and JPEG photo, respectively.
The thumbnail binary data size must be less than 5 kB (1 kB = 1000 bytes).
The photo binary data size must be less than 500 kB.

Produces an array of 1 Event object which is stored in the backend database after replacement
(if the new event data has either new `organizerEmail` and `time` field values not yet used,
or these field values remain the same),
or an empty array (if such organizer email and time were already used in other event before).

If the event photo is replaced with the new one, the `photo` and `thumbnail` fields in the Event
object in the response contain new photo and thumbnail IDs instead of Base64 data, respectively.

#### /attendances/toggle/active

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/get

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/get/for/event/active/check/voted

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Attendance | Yes |

#### /attendances/put

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/delete

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Attendance | Yes |

#### /couples/generate/for/event

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Couple | Yes |

#### /couples/put

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Couple | Couple | Yes |

#### /couples/get/for/attendance

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Couple | Yes |

#### /couples/get/for/event

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Event | Couple | Yes |

#### /ratings/get/for/attendance/active

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Attendance | Rating | Yes |

#### /ratings/put/actual

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Rating | Rating | Yes |

#### /ratings/put

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Rating | Rating | Yes |

#### /mail/send

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Email | Email | Yes |

#### /mail/request/organizer

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Email | Email | No |

#### /mail/reset/password

| Consumes array of | Produces array of | Basic Authorization |
| ----------------- | ----------------- | ------------------- |
| Email | Email | No |


### Special API methods

The Special API methods have at least one characteristic different from the General methods
(for example, a Special method can be an HTTP GET method, not a POST one).
The characteristics not mentioned are the same as for the General methods.

***Note.*** GET methods do not consume content in requests,
but still may require HTTP headers described above in characteristics.

The list of Special method paths:

#### /users/activate/pending/organizer/token

| HTTP Method | Produces (Content-Type header) | Basic Authorization |
| ----------- | ------------------------------ | ------------------- |
| GET | *Plain text* (`Content-Type: text/plain;charset=utf-8`) | No |

Used only by admin and developers to activate the pending organizer.

The `token` is a secret number randomly generated by the backend.
Developers can find it after the last `_` in the pending organizer's password
by using the Special method `/_ah/admin` when running the local (Developer) backend.
In the production backend, the `token` is not known by anyone but the admin
who receives it from the backend in an email.

Removes the `_token` part from the organizer's password and recovers the original password.
After that the organizer can log in with his password.
The method also changes the organizer's `group` from `pendingOrganizer` to `organizer`.

#### /images/get/thumbnail/path

| HTTP Method | Produces (Content-Type header) | Basic Authorization |
| ----------- | ------------------------------ | ------------------- |
| GET | *JPEG image* (`Content-Type: image/jpeg`) | No |

Returns the small sized JPEG image, a preview (thumbnail) of the full-sized image.
The `path` part of the whole path is the thumbnail ID
which a User or Event object can contain as the value of the field `thumbnail`.
The `path` is randomly generated by the backend when the thumbnail is uploaded.

#### /images/get/path

| HTTP Method | Produces (Content-Type header) | Basic Authorization |
| ----------- | ------------------------------ | ------------------- |
| GET | *JPEG image* (`Content-Type: image/jpeg`) | No |

Returns the full-sized JPEG image. The `path` part of the whole path is the image ID
which a User or Event object can contain as the value of the field `photo`.
The `path` is randomly generated by the backend when the image is uploaded.

#### /_ah/admin

This path should be used in the URL in browser, only when running the local (Developer) backend.
It provides access to the Google App Engine Development Console
where in Datastore Viewer the developer can see and delete the entities stored at the backend.