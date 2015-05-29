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
| maxRatingsPerUser | String | Maximal quantity of other "normal users" to whom a "normal user" can give votes (positive ratings) at the event. An integer number. The default value is 1 |
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

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | User | Yes |

#### /users/remove/attendance

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | User | Yes |

#### /users/get/for/event/active

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | User | Yes |

#### /users/get/for/event

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | User | Yes |

#### /users/get/for/event/lock

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | User | Yes |

#### /users/get

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | Yes |

#### /users/get/login

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | No |

#### /users/add

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | No |

#### /users/add/pending/organizer

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | No |

#### /users/add/by/organizer

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | Yes |

#### /users/replace

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | User | Yes |

#### /users/get/other/for/event

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | User | Yes |

#### /events/get

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/get/for/time

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/get/for/attendance/active

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Event | Yes |

#### /events/get/for/user

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | Event | Yes |

#### /events/get/all/for/user

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| User | Event | Yes |

#### /events/add

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/put

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/delete

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/set/unactual

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/set/user/ratings/allow

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /events/replace

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Event | Yes |

#### /attendances/toggle/active

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/get

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/get/for/event/active/check/voted

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Attendance | Yes |

#### /attendances/put

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Attendance | Yes |

#### /attendances/delete

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Attendance | Yes |

#### /couples/generate/for/event

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Couple | Yes |

#### /couples/put

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Couple | Couple | Yes |

#### /couples/get/for/attendance

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Couple | Yes |

#### /couples/get/for/event

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Event | Couple | Yes |

#### /ratings/get/for/attendance/active

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Attendance | Rating | Yes |

#### /ratings/put/actual

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Rating | Rating | Yes |

#### /ratings/put

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Rating | Rating | Yes |

#### /mail/send

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Email | Email | Yes |

#### /mail/request/organizer

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
| Email | Email | No |

#### /mail/reset/password

| Consumes | Produces | Basic Authorization |
| -------- | -------- | ------------------- |
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

#### /images/get/thumbnail/path

| HTTP Method | Produces (Content-Type header) | Basic Authorization |
| ----------- | ------------------------------ | ------------------- |
| GET | *JPEG image* (`Content-Type: image/jpeg`) | No |

#### /images/get/path

| HTTP Method | Produces (Content-Type header) | Basic Authorization |
| ----------- | ------------------------------ | ------------------- |
| GET | *JPEG image* (`Content-Type: image/jpeg`) | No |
