const BaseUrl = 'http://g9fsde.natappfree.cc'

function imageUrl(fileId) {
  return `${BaseUrl}/word/wordModuleImage/${fileId}`;
}

function imageId(url) {
  const arr = url.split("=");
  return arr[1];
}

module.exports = { imageUrl, imageId };
