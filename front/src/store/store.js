export const registerCourse = (course) => ({
  type: "REGISTER_COURSE",
  payload: course,
});
export const resetCourse = () => ({ type: "RESET_COURSE" });
export const setPlaces = (places) => ({
  type: "SET_PLACES",
  payload: places,
});
export const resetPlaces = () => ({ type: "RESET_PLACES" });
export const setMap = (map) => ({ type: "SET_MAP", payload: map });
export const setMarker = (marker) => ({ type: "SET_MARKER", payload: marker });
export const clearMarker = () => ({ type: "CLEAR_MARKERS" });
export const getUserInfo = (data) => ({ type: "GET_USERINFO", payload: data });
export const logoutUser = () => ({ type: "LOGOUT_USER" });
export const setSideMenu = (data) => ({ type: "SET_SIDEMENU", payload: data });
export const toggleUserMenu = () => ({ type: "TOGGLE_USERMENU" });
export const toggleMyCourseMenu = (data) => ({
  type: "TOGGLE_MY_COURSE_MENU",
  payload: data,
});
export const toggleSavedCourseMenu = (data) => ({
  type: "TOGGLE_SAVED_COURSE_MENU",
  payload: data,
});
export const togglePhotoModal = () => ({ type: "TOGGLE_PHOTOMODAL" });
export const setPreviewPhotos = (photo) => ({
  type: "SET_PREVIEW_PHOTOS",
  payload: photo,
});
export const clearPreviewPhotos = () => ({ type: "CLEAR_PREVIEW_PHOTOS" });
export const setLocation = (location) => ({
  type: "SET_LOCATION",
  payload: location,
});
export const clearLocation = () => ({ type: "CLEAR_LOCATION" });
export const setSearchCourseList = (data) => ({
  type: "SET_SEARCH_COURSE_LIST",
  payload: data,
});
export const setSelectedDatecourse = (course) => ({
  type: "SET_SELECTED_DATECOURSE",
  payload: course,
});
export const setSelectedDatecourseIndex = (key) => ({
  type: "SET_SELECTED_DATECOURSE_INDEX",
  payload: key,
});
export const setUserLikedCourse = (data) => ({
  type: "SET_USER_LIKED_COURSE",
  payload: data,
});
export const setUserSavedCourse = (data) => ({
  type: "SET_USER_SAVED_COURSE",
  payload: data,
});
export const setSearchOption = (data) => ({
  type: "SET_SEARCH_OPTION",
  payload: data,
});
export const setMyCourseList = (data) => ({
  type: "SET_MY_COURSE_LIST",
  payload: data,
});
export const setSavedCourseList = (data) => ({
  type: "SET_SAVED_COURSE_LIST",
  payload: data,
});

const initstate = {
  course: [],
  places: [],
  marker: [],
  map: null,
  user: null,
  sideMenu: false,
  userMenu: false,
  myCourseMenu: false,
  savedCourseMenu: false,
  photoModal: false,
  previewPhotos: [],
  location: {
    place: {},
  },
  searchCourseList: [],
  selectedDatecourse: null,
  selectedDatecourseIndex: 0,
  userLikedCourse: null,
  userSavedCourse: null,
  searchOption: "recent",
  myCourseList: null,
  savedCourseList: null,
};

const reducer = (state = initstate, action) => {
  switch (action.type) {
    case "REGISTER_COURSE":
      return { ...state, course: [...state.course, action.payload] };
    case "RESET_COURSE":
      return { ...state, course: [] };
    case "SET_PLACES":
      return { ...state, places: [...action.payload] };
    case "RESET_PLACES":
      return { ...state, places: [] };
    case "SET_MAP":
      return { ...state, map: action.payload };
    case "SET_MARKER":
      return { ...state, marker: [...state.marker, action.payload] };
    case "CLEAR_MARKERS":
      return { ...state, marker: [] };
    case "GET_USERINFO":
      return { ...state, user: action.payload };
    case "LOGOUT_USER":
      return { ...state, user: null };
    case "TOGGLE_USERMENU":
      return { ...state, userMenu: !state.userMenu };
    case "SET_SIDEMENU":
      return { ...state, sideMenu: action.payload };
    case "TOGGLE_PHOTOMODAL":
      return { ...state, photoModal: !state.photoModal };
    case "SET_PREVIEW_PHOTOS":
      return {
        ...state,
        previewPhotos: [...state.previewPhotos, action.payload],
      };
    case "CLEAR_PREVIEW_PHOTOS":
      return { ...state, previewPhotos: [] };
    case "TOGGLE_MY_COURSE_MENU":
      return { ...state, myCourseMenu: action.payload };
    case "TOGGLE_SAVED_COURSE_MENU":
      return { ...state, savedCourseMenu: action.payload };
    case "SET_LOCATION":
      return {
        ...state,
        location: { place: action.payload },
      };
    case "CLEAR_LOCATION":
      return {
        ...state,
        location: {
          place: {},
        },
      };
    case "SET_SEARCH_COURSE_LIST":
      return {
        ...state,
        searchCourseList: action.payload,
      };
    case "SET_SELECTED_DATECOURSE":
      return {
        ...state,
        selectedDatecourse: action.payload,
      };
    case "SET_SELECTED_DATECOURSE_INDEX":
      return {
        ...state,
        selectedDatecourseIndex: action.payload,
      };
    case "SET_USER_LIKED_COURSE":
      return {
        ...state,
        userLikedCourse: action.payload,
      };
    case "SET_USER_SAVED_COURSE":
      return {
        ...state,
        userSavedCourse: action.payload,
      };
    case "SET_SEARCH_OPTION":
      return {
        ...state,
        searchOption: action.payload,
      };
    case "SET_MY_COURSE_LIST":
      return {
        ...state,
        myCourseList: action.payload,
      };
    case "SET_SAVED_COURSE_LIST":
      return {
        ...state,
        savedCourseList: action.payload,
      };
    default:
      return state;
  }
};

export default reducer;
