const BaseUrl = 'http://4x3yuu.natappfree.cc'

function imageUrl(fileId) {
  return `${BaseUrl}/word/wordModuleImage/${fileId}`;
}

function avatarUrl(userId) {
  return `${BaseUrl}/user/userImage/${userId}`;
}

function imageId(url) {
  const arr = url.split("=");
  return arr[1];
}

module.exports = { imageUrl,avatarUrl, imageId };
