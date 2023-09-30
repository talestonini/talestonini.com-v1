import * as d3 from "d3";
import { cloud } from "d3-cloud";

const width = 1000;
const height = 1000;

// reference:
// https://observablehq.com/@zhoiii/d3-wordcolud
export function drawWordCloud(words) {
  const fontFamily = "Verdana, Arial, Helvetica, sans-serif";

  const svg = d3.select("#tags").append("svg")
    .attr("id", "word-cloud")
    .attr("viewBox", [0, 0, width, height])
    .attr("font-family", fontFamily)
    .attr("text-anchor", "middle");

  const c = cloud()
    .size([width, height])
    .words(words.map(function(d) {
      return { text: d, size: 10 + Math.random() * 90 };
    }))
    .padding(1)
    .rotate(function() { return ~~(Math.random() * 3) * 30; })
    .font(fontFamily)
    .timeInterval([1])
    .fontSize(function(d) { return d.size; })
    .on("word", ({ size, x, y, rotate, text }) => {
      svg.append("text")
        .attr("font-size", size)
        .attr("transform", `translate(${x},${y}) rotate(${rotate})`)
        .attr("data-counts", text)
        .text(text)
        .style("fill", d3.interpolateRdYlGn(Math.random()))
        .classed("click-only-text", true)
        .classed("word-default", true)
        .on("mouseover", handleMouseOver)
        .on("mouseout", handleMouseOut)
        .on("click", handleClick);

      function handleMouseOver(d, i) {
        d3.select(this)
          .classed("word-hovered", true)
          .transition(`mouseover-${text}`).duration(300).ease(d3.easeLinear)
          .attr("font-size", size + 2)
          .attr("font-weight", "bold");
      }

      function handleMouseOut(d, i) {
        d3.select(this)
          .classed("word-hovered", false)
          .interrupt(`mouseover-${text}`)
          .attr("font-size", size);
      }

      function handleClick(d, i) {
        var e = d3.select(this);
        console.log(">>> clicked on: " + e.text());
        e.classed("word-selected", !e.classed("word-selected"));
      }
    });

  c.start();
}

export function drawWordCloudOld(words) {
  var layout = cloud()
    .size([500, 500])
    .words(words.map(function(d) {
      return { text: d, size: 10 + Math.random() * 90 };
    }))
    .padding(5)
    .rotate(function() { return ~~(Math.random() * 2) * 90; })
    .font("Impact")
    .fontSize(function(d) { return d.size; })
    .on("end", draw);

  layout.start();

  function draw(words) {
    d3.select("#tags").append("svg")
      .attr("width", layout.size()[0])
      .attr("height", layout.size()[1])
      .append("g")
      .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
      .selectAll("text")
      .data(words)
      .enter().append("text")
      .style("font-size", function(d) { return d.size + "px"; })
      .style("font-family", "Impact")
      .attr("text-anchor", "middle")
      .attr("transform", function(d) {
        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
      })
      .text(function(d) { return d.text; });
  }
}
