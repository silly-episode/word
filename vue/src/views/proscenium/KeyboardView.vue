<template>
  <div class="bgcolor-off">
    <div class="container">
      <h1 class="title title-off">Cool keyboard game</h1>
      <!-- The score will change each time you press the right key -->
      <h1 class="title title-off" id="score">Score : 0</h1>

      <!-- Almost an AZERTY keyboard -->
      <div class="keyboard">
        <!-- First row : ESC, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, -, + and BACK -->
        <ul class="row row-0">
          <li class="color0-off 0" id="esc">ESC</li>
          <li class="color0-off 0" id="1">1</li>
          <li class="color1-off 1" id="2">2</li>
          <li class="color2-off 2" id="3">3</li>
          <li class="color3-off 3" id="4">4</li>
          <li class="color4-off 4" id="5">5</li>
          <li class="color4-off 4" id="6">6</li>
          <li class="color3-off 3" id="7">7</li>
          <li class="color2-off 2" id="8">8</li>
          <li class="color1-off 1" id="9">9</li>
          <li class="color0-off 0" id="0">0</li>
          <li class="color0-off 0">-</li>
          <li class="color0-off 0">+</li>
          <li class="color0-off 0" id="back">BACK</li>
        </ul>
        <!-- Second row : TAB, A, Z, E, R, T, Y, U, I, O, P, "[", "]" and "\" -->
        <ul class="row row-1">
          <li class="color0-off 0" id="tab">TAB</li>
          <li class="color0-off 0" id="A">A</li>
          <li class="color1-off 1" id="Z">Z</li>
          <li class="color2-off 2" id="E">E</li>
          <li class="color3-off 3" id="R">R</li>
          <li class="color4-off 4" id="T">T</li>
          <li class="color4-off 4" id="Y">Y</li>
          <li class="color3-off 3" id="U">U</li>
          <li class="color2-off 2" id="I">I</li>
          <li class="color1-off 1" id="O">O</li>
          <li class="color0-off 0" id="P">P</li>
          <li class="color0-off 0">[</li>
          <li class="color0-off 0">]</li>
          <li class="color0-off 0">\</li>
        </ul>
        <!-- Third row : CAPS, Q, S, D, F, G, H, J, K, L, M, "%" and ENTER -->
        <ul class="row row-2">
          <li class="color0-off 0" id="caps">CAPS</li>
          <li class="color0-off 0" id="Q">Q</li>
          <li class="color1-off 1" id="S">S</li>
          <li class="color2-off 2" id="D">D</li>
          <li class="color3-off 3" id="F">F</li>
          <li class="color4-off 4" id="G">G</li>
          <li class="color4-off 4" id="H">H</li>
          <li class="color3-off 3" id="J">J</li>
          <li class="color2-off 2" id="K">K</li>
          <li class="color1-off 1" id="L">L</li>
          <li class="color0-off 0" id="M">M</li>
          <li class="color0-off 0">%</li>
          <li class="color0-off 0" id="enter">ENTER</li>
        </ul>
        <!-- Fourth row : LEFT-SHIFT, W, X, C, V, B, N, "?", ".", ";", ":" and RIGHT-SHIFT -->
        <ul class="row row-3">
          <li class="color0-off 0" id="shift">SHIFT</li>
          <li class="color0-off 0" id="W">W</li>
          <li class="color1-off 1" id="X">X</li>
          <li class="color2-off 2" id="C">C</li>
          <li class="color3-off 3" id="V">V</li>
          <li class="color4-off 4" id="B">B</li>
          <li class="color4-off 4" id="N">N</li>
          <li class="color3-off 3">?</li>
          <li class="color2-off 2">.</li>
          <li class="color1-off 1">;</li>
          <li class="color0-off 0">:</li>
          <li class="color0-off 0" id="shift">SHIFT</li>
        </ul>
      </div>

      <!-- Combo text that will change when you have 5, 10, 20 and 50 points -->
      <h1 class="title title-off" id="combo"></h1>
    </div>

    <!-- Funny beer pong button -->
    <div class="toggle beer-pong">
      <input id="beer-pong" type="checkbox" onclick="toggleClick()" />
      <label class="toggle-item" for="beer-pong"></label>
      <div class="cup">
        <div class="lid"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

*:before,
*:after {
  content: "";
  position: absolute;
}

body {
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-weight: 500;
  transition: background-color 1s;
}

.bgcolor-off {
  background-color: black;
}

.bgcolor-on {
  background-color: #c6c5c5;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.title {
  text-transform: uppercase;
  margin-top: 3em;
  margin-bottom: 3em;
  font-size: 1em;
  letter-spacing: 0.3em;

  transition: color 1s;
}

.title-off {
  color: mintcream;
}

.title-on {
  color: rgba(0, 0, 0, 0.7);
}

.keyboard {
  display: flex;
  flex-direction: column;
}

.row {
  list-style: none;
  display: flex;
}

li {
  height: 3em;
  width: 3em;
  border-radius: 0.4em;
  line-height: 3em;
  letter-spacing: 1px;
  margin: 0.4em;
  transition: 0.3s;
  text-align: center;
  font-size: 1em;

  transition: background-color 1s, border 1s, color 1s;
}

#back,
#tab {
  width: 5em;
}

#caps,
#enter {
  width: 6em;
}

#shift {
  width: 8em;
}

.color0-off {
  background-color: #ed7b5f;
  border: 2px solid #ed7b5f;
  color: rgba(0, 0, 0, 0.7);
}

.color0-off.selected {
  color: #ed7b5f;
}

.color1-off {
  background-color: #ea9556;
  border: 2px solid #ea9556;
  color: rgba(0, 0, 0, 0.7);
}

.color1-off.selected {
  color: #ea9556;
}

.color2-off {
  background-color: #e1af58;
  border: 2px solid #e1af58;
  color: rgba(0, 0, 0, 0.7);
}

.color2-off.selected {
  color: #e1af58;
}

.color3-off {
  background-color: #d3c766;
  border: 2px solid #d3c766;
  color: rgba(0, 0, 0, 0.7);
}

.color3-off.selected {
  color: #d3c766;
}

.color4-off {
  background-color: #c1de81;
  border: 2px solid #c1de81;
  color: rgba(0, 0, 0, 0.7);
}

.color4-off.selected {
  color: #c1de81;
}

.color0-on {
  background-color: #0d2e60;
  border: 2px solid #0d2e60;
  color: rgba(255, 255, 255, 0.7);
}

.color0-on.selected {
  color: #0d2e60;
}

.color1-on {
  background-color: #224882;
  border: 2px solid #224882;
  color: rgba(255, 255, 255, 0.7);
}

.color1-on.selected {
  color: #224882;
}

.color2-on {
  background-color: #3564a5;
  border: 2px solid #3564a5;
  color: rgba(255, 255, 255, 0.7);
}

.color2-on.selected {
  color: #3564a5;
}

.color3-on {
  background-color: #4882ca;
  border: 2px solid #4882ca;
  color: rgba(255, 255, 255, 0.7);
}

.color3-on .selected {
  color: #4882ca;
}

.color4-on {
  background-color: #59a1f0;
  border: 2px solid #59a1f0;
  color: rgba(255, 255, 255, 0.7);
}

.color4-on.selected {
  color: #59a1f0;
}

.godlike {
  color: red;
  scale: 1.5;
}

.fill-out-key {
  background-color: slategrey;
  border: 2px solid slategrey;
}

.selected {
  background-color: transparent;
  -webkit-animation: vibrate-1 0.3s linear infinite both;
  animation: vibrate-1 0.3s linear infinite both;
}

.hit {
  -webkit-animation: hit 0.3s cubic-bezier(0.39, 0.575, 0.565, 1) both;
  animation: hit 0.3s cubic-bezier(0.39, 0.575, 0.565, 1) both;
}

@-webkit-keyframes hit {
  0% {
    -webkit-transform: scale(1.2);
    transform: scale(1.2);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}

@keyframes hit {
  0% {
    -webkit-transform: scale(1.2);
    transform: scale(1.2);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}

@-webkit-keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
    transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
    transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
    transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
    transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
}

@keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
    transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
    transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
    transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
    transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
}

input {
  height: 40px;
  left: 0;
  opacity: 0;
  position: absolute;
  top: 0;
  width: 40px;
}

.toggle {
  position: relative;
  display: inline-block;
}

label.toggle-item {
  width: 7em;
  background: #2e394d;
  height: 3em;
  display: inline-block;
  border-radius: 50px;
  margin: 40px;
  position: relative;
  transition: all 0.3s ease;
  transform-origin: 20% center;
  cursor: pointer;
}

label.toggle-item:before {
  display: block;
  transition: all 0.2s ease;
  width: 2.3em;
  height: 2.3em;
  top: 0.25em;
  left: 0.25em;
  border-radius: 2em;
  border: 2px solid #88cf8f;
  transition: 0.3s ease;
}

.beer-pong {
  position: absolute;
  bottom: 0;
  left: 0;
}

.beer-pong label:before {
  background: #f9f9f9;
  box-shadow: inset 0 -3px 0 0 #c6c5c5;
  border: none;
  width: 2.5em;
  height: 2.5em;
  top: 0.25em;
  left: 0.25em;
}

.beer-pong .cup {
  top: -3%;
  right: -118px;
  border-top: 90px solid #f44336;
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  height: 3px;
  position: absolute;
  width: 80px;
  transform-origin: bottom right;
  transition: 0.2s cubic-bezier(0.42, 0.5, 0.58, 1);
}

.beer-pong .cup:before {
  box-shadow: 0 -10px 0 0px rgba(39, 39, 39, 0.1),
    0px -20px 0 0px rgba(39, 39, 39, 0.1);
  border-radius: 3px;
  overflow: hidden;
  background: rgba(39, 39, 39, 0.1);
  width: 120%;
  left: -5px;
  height: 4px;
  top: -40px;
}

.beer-pong .cup .lid {
  position: absolute;
  width: 95px;
  height: 8px;
  border-radius: 20px;
  background: #efefef;
  bottom: 86px;
  left: -23px;
}

.beer-pong .cup .lid:after {
  background: #efefef;
  width: 48px;
  height: 5px;
  left: 50%;
  margin-left: -24px;
  top: 94px;
  border-radius: 0 0 3px 3px;
}

#beer-pong:checked ~ .cup {
  animation: 0.2s linear cup 1s forwards;
}

#beer-pong:checked + label:before {
  animation: 2s linear bounce-off forwards;
}

@keyframes cup {
  0% {
    transform: none;
  }
  50% {
    transform: rotate(75deg) translate(10px, 15px);
  }
  90% {
    transform: rotate(70deg) translate(10px, 15px);
  }
  100% {
    transform: rotate(75deg) translate(10px, 15px);
  }
}

@keyframes bounce-off {
  0% {
    transform: translateY(0);
  }
  10%,
  25% {
    transform: translate(-20px, -80px);
  }
  50% {
    transform: rotate(163deg);
    transform-origin: 100px -12px;
  }
  70% {
    transform: rotate(0) translate(-3px, -8px);
    transform-origin: 100px -12px;
  }
  75% {
    transform: translate(20px, -8px);
  }
  80% {
    transform: translate(30px, 0px);
  }
  85% {
    transform: translate(40px, -3px);
  }
  87% {
    transform: translate(46px, 0px);
  }
  90% {
    transform: translate(52px, -1px);
  }
  95% {
    transform: translate(60px, 0px);
  }
  100% {
    transform: translate(64px, 0px);
  }
}
</style>