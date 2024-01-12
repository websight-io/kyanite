export {};

declare global {
  interface Window {
    KYANITE_ON_LOAD: string;
    KYANITE_ON_DOM_CONTENT_LOAD: string;
  }
}

window.KYANITE_ON_LOAD = window.KYANITE_ON_LOAD || 'load';
window.KYANITE_ON_DOM_CONTENT_LOAD = window.KYANITE_ON_DOM_CONTENT_LOAD || 'DOMContentLoaded';


console.log(window.KYANITE_ON_LOAD);
console.log(window.KYANITE_ON_DOM_CONTENT_LOAD);