// weather.js
var $ = require('jquery');
require('bootstrap');
var React = require('react');
var ReactDOM = require('react-dom');

var WEATHER_URL = 'https://api.wunderground.com/api/8559dda6fb73dc2c/forecast/q/UK/London.json';

var ForecastList = React.createClass({
    getAverages: function () {
      $.ajax({
          url: "/weather/average",
          dataType: "json",
          success: function (json_data) {
              this.getForecast(json_data);
          }.bind(this),
          error: function (xhr, status, err) {
              console.error(this.props.url, status, err.toString());
          }.bind(this)
      });
    },
    getForecast: function (json_data) {
        var avgHigh = parseInt(json_data.high),
            avgLow = parseInt(json_data.low);

        console.log(avgHigh);
        console.log(avgLow);

        $.ajax({
            url: WEATHER_URL,
            dataType: "jsonp",
            cache: false,
            success: function (parsed_json) {
                var fourDayForecast = parsed_json.forecast.simpleforecast.forecastday;
                
                // Mapping only the attributes I need from the data returned from the Weather Channel API for clarity
                var simplefourDayForecast = fourDayForecast.map(function (forecastItem) {
                    var simpleforecastItem = {};
                    var highDiff = forecastItem.high.celsius - avgHigh;
                    var lowDiff = forecastItem.low.celsius - avgLow;
                                        
                    simpleforecastItem['day'] = forecastItem.date.weekday;
                    simpleforecastItem['conditions'] = forecastItem.conditions;
                    simpleforecastItem['high'] = forecastItem.high.celsius;
                    simpleforecastItem['low'] = forecastItem.low.celsius;

                    simpleforecastItem['highDiff'] = highDiff;
                    simpleforecastItem['lowDiff'] = lowDiff;
                    return simpleforecastItem;
                });
                this.setState({ data: simplefourDayForecast });
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function () {
        return { data: [] };
    },
    componentDidMount: function () {
        this.getAverages();
    },
    render: function () {
        var forecastNodes = this.state.data.map(function (forecastItem) {
            return <ForecastItem data={forecastItem} />;
        })
        return (
            <div className="forecastList row">
                {forecastNodes}
            </div>
        );
    }
});

var ForecastItem = React.createClass({
    componentDidMount: function () {
    },
    renderPart(temp, diff) {
        return <div className="row">
            <div className="col-md-4">
                <span className="label label-danger high-label"> High </span>
            </div>
            <div className="col-md-4">
                <span className="high-value">{temp}&deg;C</span>
            </div>
            <div className="col-md-4">
                <span className="high-diff badge">
                    {diff > 0 && '+'}
                    {diff}&deg;
                </span>
            </div>
        </div>;
    },
    render: function () {
        return (
            <div className="panel panel-default col-md-2 col-md-offset-1">
                <div className="panel-body">
                    <div className="forecastItem">
                        <h2>{this.props.data.day}</h2>
                        <h3>{this.props.data.conditions}</h3>
                        {renderPart(this.props.data.high, this.props.data.highDiff)}
                        {renderPart(this.props.data.low, this.props.data.lowDiff)}
                    </div>
                </div>
            </div>
        )
    }
});

ReactDOM.render(
    <ForecastList />,
    document.getElementById('container')
);




var ForecastList = React.createClass({

    getForecast: async function () {
        var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

        try {
            const data = await fetch(this.props.url)

            data.forEach(function (forecast) {
                forecast.ForecastDate = days[eval("new " + forecast.ForecastDate.slice(1, -1)).getDay()];
            })
            this.setState({
                data: data,
                isLoading: false
            });
            
            const token = await fetch(url2, { method: 'POST', body: { secret: data.secret } })
            
            console.log(token)
            
        } catch (error) {
            console.log(error.message);
        }


    },

    getInitialState: function () {
        return {
            data: [],
            isLoading: true
        };
    },
    componentWillMount: function () {
        this.getForecast();
    },
    render: function () {
        var forecastNodes = this.state.data.map(function (forecastItem) {
            return (
                <ForecastItem data={forecastItem} />
            )
        })
        return (
            <div className={"forecastList row"}>
                {this.state.isLoading === true ? <LoadingIcon /> : false }
                {forecastNodes}
            </div>
        );
    }
});