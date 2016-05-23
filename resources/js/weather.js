// weather.js
var jQuery = require('jquery');
window.$ = jQuery;
require('bootstrap');
var React = require('react');
var ReactDOM = require('react-dom');

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
            url: "https://api.wunderground.com/api/8559dda6fb73dc2c/forecast/q/UK/London.json",
            dataType: "jsonp",
            cache: false,
            success: function (parsed_json) {
                var fourDayForecast = parsed_json.forecast.simpleforecast.forecastday;
                
                // Mapping only the attributes I need from the data returned from the Weather Channel API for clarity
                var simplefourDayForcast = fourDayForecast.map(function (forecastItem) {
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
                this.setState({ data: simplefourDayForcast });
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
            return (
                <ForecastItem data={forecastItem} />
            )
        })
        return (
            <div className={"forcastList row"}>
                {forecastNodes}
            </div>
        );
    }
});

var ForecastItem = React.createClass({
    componentDidMount: function () {
        // Small bit of centering of the panels
        $('.panel:first').removeClass("col-md-offset-1").css({ 'margin-left': '4%' });
    },
    render: function () {
        return (
            <div className={"panel panel-default col-md-2 col-md-offset-1"}>
                <div className={"panel-body"}>
                    <div className={"forecastItem"}>
                        <h2>{this.props.data.day}</h2>
                        <h3>{this.props.data.conditions}</h3>
                        <div className={"row"}>
                            <div className={"col-md-4"}>
                                <span className={"label label-danger high-label"}> High </span>
                            </div>
                            <div className={"col-md-4"}>
                                <span className={"high-value"}>{this.props.data.high}&deg;C</span>
                            </div>
                            <div className={"col-md-4"}>
                                {this.props.data.highDiff > 0 ? <span className={"high-diff badge"}>+{this.props.data.highDiff}&deg;</span> : false}
                                {this.props.data.highDiff < 0 ? <span className={"high-diff badge"}>{this.props.data.highDiff}&deg;</span> : false}
                            </div>
                        </div>
                        <div className={"row"}>
                            <div className={"col-md-4"}>
                                <span className={"label label-info low-label"}> Low </span>
                            </div>
                            <div className={"col-md-4"}>
                                <span className={"low-value"}>{this.props.data.low}&deg;C</span>
                            </div>
                            <div className={"col-md-4"}>
                                {this.props.data.lowDiff > 0 ? <span className={"low-diff badge"}>+{this.props.data.lowDiff}&deg;</span> : false}
                                {this.props.data.lowDiff < 0 ? <span className={"low-diff badge"}>{this.props.data.lowDiff}&deg;</span> : false}
                            </div>
                        </div>
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