/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/3 22:34
 * @FileName: monitorZoom.js
 * @Description:
 */
export const monitorZoom = () => {
    let ratio = 0;
    let screen = window.screen;
    let ua = navigator.userAgent.toLowerCase();

    if (window.devicePixelRatio !== undefined) {
        ratio = window.devicePixelRatio;
    } else if (~ua.indexOf("msie")) {
        if (screen.deviceXDPI && screen.logicalXDPI) {
            ratio = screen.deviceXDPI / screen.logicalXDPI;
        }
    } else if (
        window.outerWidth !== undefined &&
        window.innerWidth !== undefined
    ) {
        ratio = window.outerWidth / window.innerWidth;
    }
    if (ratio) {
        ratio = Math.round(ratio * 100 / 1.25);
    }
    console.log(ratio)
    return ratio;
};
