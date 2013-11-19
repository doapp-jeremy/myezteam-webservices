myezteam-webservices
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

## Team Resources

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

### GET /teams/{team_id}
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

### PUT /teams
#### Update a team
#### Example data
```
{
    "id": 3,
    "name": "Sloppy Waffles",
    "type": "broomball",
    "default_location": "Withers",
    "description": "Outdoor broomball team"
}
```

### GET /teams/{team_id}/managers
#### Get a team's managers
```
[
    {
        "id": 64,
        "email": "boecker@us.ibm.com",
        "first_name": "Doug",
        "last_name": "Boecker"
    },
    {
        "id": 105,
        "email": "michael.burman@gmail.com",
        "first_name": "Mike",
        "last_name": "Burman"
    },
    {
        "id": 129,
        "email": "nathan.rabe@gmail.com",
        "first_name": "Nate",
        "last_name": "Rabe"
    }
]
```

### POST /teams/{team_id}/managers
#### Add a manager to a team
#### Example data
```
[ 3, 7]
```

### DELETE /teams/{team_id}/managers/{user_id}
#### Remove manager from team

## User Resource

### GET /users
#### Get my info
```
{
    "id": 3,
    "email": "junker37@gmail.com",
    "first_name": "Jeremy",
    "last_name": "McJunkin"
}
```

### PUT /users
#### Update the logged in users personal info
```
{
  "id": 3,
  "first_name": "test",
  "last_name": "asdfadsf",
  "email":"junker37@gmail.com"
}
```

## Player Resource

### GET /players
#### Get's all of the user's players
```
[
    {
        "id": 68,
        "user_id": 3,
        "team_id": 3,
        "user": null,
        "team": {
            "id": 3,
            "name": "Sloppy Waffles",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": "Regular"
    },
    {
        "id": 269,
        "user_id": 3,
        "team_id": 38,
        "user": null,
        "team": {
            "id": 38,
            "name": "IBM Coed BLUE Team",
            "type": null,
            "default_location": null,
            "description": null
        },
        "player_type": "Sub"
    }
]
```

### GET /players/team/{team_id}
#### Get players on a team
```
[
    {
        "id": 68,
        "team_id": 3,
        "user": {
            "id": 3,
            "email": "junker37@gmail.com",
            "first_name": "Jeremy",
            "last_name": "McJunkin"
        },
        "player_type": "Regular"
    },
    {
        "id": 51,
        "team_id": 3,
        "user": {
            "id": 64,
            "email": "boecker@us.ibm.com",
            "first_name": "Doug",
            "last_name": "Boecker"
        },
        "player_type": "Regular"
    }
]
```

### POST /players/team/{team_id}
#### Example data
```
{
  "team_id": 3,
  "user_id": 129,
  "player_type_id": 2
}
```
#### Response
```
[
    {
        "id": 68,
        "user_id": 3,
        "team_id": 3,
        "user": {
            "id": 3,
            "email": "junker37@gmail.com",
            "first_name": "Jeremy",
            "last_name": "McJunkin"
        },
        "player_type": "Regular",
        "player_type_id": 1
    },
    {
        "id": 51,
        "user_id": 64,
        "team_id": 3,
        "user": {
            "id": 64,
            "email": "boecker@us.ibm.com",
            "first_name": "Doug",
            "last_name": "Boecker"
        },
        "player_type": "Regular",
        "player_type_id": 1
    },
    {
        "id": 1221,
        "user_id": 129,
        "team_id": 3,
        "user": {
            "id": 129,
            "email": "nathan.rabe@gmail.com",
            "first_name": "Nate",
            "last_name": "Rabe"
        },
        "player_type": "Sub",
        "player_type_id": 2
    }
]
```

### DELETE /players/team/{team_id}/{player_id}
#### Remove player from team

### PUT /players/team/{team_id}/{player_id}/{player_type_id}
#### Change player type

## Events

### GET /events
#### Get user's next 3 upcoming events
#### Response
```
[
    {
        "id": 1253,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-15",
        "end": "2013-11-15",
        "description": "",
        "location": "Graham Arena Rink #4"
    },
    {
        "id": 1254,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-22",
        "end": "2013-11-22",
        "description": "",
        "location": "Graham Arena Rink #4"
    },
    {
        "id": 1255,
        "name": "Pickup Games",
        "team_id": 29,
        "start": "2013-11-29",
        "end": "2013-11-29",
        "description": "",
        "location": "Graham Arena Rink #4"
    }
]
```

### GET /events/{event_id}
#### Get an event
#### Response
```
{
    "id": 1253,
    "name": "Pickup Games",
    "team_id": 29,
    "start": "2013-11-15",
    "end": "2013-11-15",
    "description": "",
    "location": "Graham Arena Rink #4"
}
```

### POST /events
#### Create an event
#### Example data
```
{
    "name": "Pickup Games",
    "team_id": 29,
    "start": "2013-12-15",
    "end": "2013-12-15",
    "description": "",
    "location": "Graham Arena Rink #4"
}
```
#### Response
```
TODO
```

### PUT /events/{event_id}
#### Update an event
#### Example data
```
```

### DELETE /events/{event_id}
#### Delete an event

```
{
    "id": 1253,
    "name": "Pickup Games - update name",
    "team_id": 29,
    "start": "2013-12-15",
    "end": "2013-12-15",
    "description": "",
    "location": "Graham Arena Rink #4"
}
```

### GET /events/{event_id}/responses
#### Gets responses for an event
#### Response
```
[
    {
        "id": 11854,
        "event_id": 1253,
        "player_id": 360,
        "response": {
            "id": 3,
            "label": "Probably"
        },
        "created": "2013-11-14"
    },
    {
        "id": 11855,
        "event_id": 1253,
        "player_id": 427,
        "response": {
            "id": 3,
            "label": "Probably"
        },
        "created": "2013-11-14"
    },
    {
        "id": 11870,
        "event_id": 1253,
        "player_id": 1080,
        "response": {
            "id": 5,
            "label": "No"
        },
        "created": "2013-11-14"
    }
]
```

### GET /events/{event_id}/emails
#### Gets emails for an event
#### Response
```
[
    {
        "id": 3216,
        "title": "Can you play?",
        "days_before": 1,
        "content": "Please respond if you can play this week.",
        "event_id": 1254,
        "include_rsvp_form": true,
        "send_type": "days_before",
        "send_on": null,
        "team_id": 0,
        "default": false
    }
]
```

### POST /events
#### Create a response
#### Example data
```
{
  "response_type_id":2,
  "event_id":1254,
  "player_id":188
}
```

### GET /responses/{event_id}
#### Get logged in user's responses for event, latest one first
#### Response
```
[
    {
        "id": 11873,
        "event_id": 1254,
        "player_id": 188,
        "response": {
            "id": 5,
            "label": "No"
        },
        "created": "2013-11-19T04:04:06.000-06:00",
        "comment": null,
        "response_type_id": 5
    },
    {
        "id": 11872,
        "event_id": 1254,
        "player_id": 188,
        "response": {
            "id": 2,
            "label": "Yes"
        },
        "created": "2013-11-19T03:57:02.000-06:00",
        "comment": null,
        "response_type_id": 2
    }
]
```

