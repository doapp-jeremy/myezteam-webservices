myezteam-werbservices
=====================

My EZ Team webservices

## Current version: v1
```
All endpoints prefixed with version, currenlty v1
ie: GET /v1/teams
```

## Api Keys
### Each request will require an api key as a query parameter, to request an api key, please email admin@myezteam.com
####
```
example: POST /v1/auth/login?api_key=a344ba35-e9b1-4360-9335-1c200f8f8d4d
```

## Authentication
### POST /auth/login
#### Example data
```
{
  "email":"junker37@gmail.com",
  "password":"myPassw0Rd"
}
```
#### Response
```
{
    "token": "c97fede5-8316-49bb-8d0a-3eeb91fe4a02",
    "user_id": 3,
    "created": 1383662745575
}
```

### GET /teams
#### Get teams a user is a player on
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/owner
#### Get teams a user owns (created)s
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/manager
#### Get teams a user manages
#### Response
```
[
    {
        "id": 3,
        "name": "Sloppy Waffles",
        "type": "broomball",
        "default_location": "Withers",
        "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
    },
    {
        "id": 21,
        "name": "Eagle Drug",
        "type": "Softball",
        "default_location": "McQuillan Field ",
        "description": ""
    }
]
```

### GET /teams/all
#### Get all teams a user plays on/owns/manages by type
#### Response
```
{
    "manager": [
        {
            "id": 173,
            "name": "Cradick Volleyball",
            "type": "Volleyball League",
            "default_location": "Cradick House",
            "description": "Open pickup volleyball games"
        }
    ],
    "player": [
        {
            "id": 3,
            "name": "Sloppy Waffles",
            "type": "broomball",
            "default_location": "Withers",
            "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
        },
        {
            "id": 21,
            "name": "Eagle Drug",
            "type": "Softball",
            "default_location": "McQuillan Field ",
            "description": ""
        }
    ],
    "owner": [
        {
            "id": 3,
            "name": "Sloppy Waffles",
            "type": "broomball",
            "default_location": "Withers",
            "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
        },
        {
            "id": 46,
            "name": "Chupacabras",
            "type": "Softball",
            "default_location": "McQuillan IV",
            "description": "Fall league softball team"
        }
    ]
}
```

### GET /teams/{id}
#### Get a team
#### Response
```
{
    "id": 3,
    "name": "Sloppy Waffles",
    "type": "broomball",
    "default_location": "Withers",
    "description": "Outdoor broomball team.\n\n2013 Schedule: \nhttp://www.maxsolutions.com/files/WebLink/Clients/Rochester/AL-Broomball/LSch_Cente_89.htm"
}
```

### POST /teams
#### Create a team
#### Example data
```
{
  "name": "My New Team",
  "type": "Broomball",
  "default_location": "Graham Arena",
  "description": "Pickup broomball games"
}
```
#### Response
```
{
    "id": 181,
    "owner_id": 3,
    "name": "My New Team",
    "type": "Broomball",
    "default_location": "Graham Arena",
    "description": "Pickup broomball games"
}
```
