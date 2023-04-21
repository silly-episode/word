function imageUrl(fileId) {
  return `api/word/wordModuleImage/${fileId}`;
}

function avatarUrl(userId) {
  return `api/user/userImage/${userId}`;
}

function imageId(url) {
  const arr = url.split("=");
  return arr[1];
}

module.exports = { imageUrl,avatarUrl, imageId };
