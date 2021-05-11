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
export const setSideMenu = () => ({ type: "SET_SIDEMENU" });
export const toggleUserMenu = () => ({ type: "TOGGLE_USERMENU" });
export const togglePhotoModal = () => ({ type: "TOGGLE_PHOTOMODAL" });
export const setLocation = (location) => ({
  type: "SET_LOCATION",
  payload: location,
});
export const clearLocation = () => ({ type: "CLEAR_LOCATION" });

const initstate = {
  course: [],
  places: [],
  marker: [],
  map: null,
  user: {},
  sideMenu: false,
  userMenu: false,
  photoModal: false,
  location: {
    place: {},
  },
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
    case "TOGGLE_USERMENU":
      return { ...state, userMenu: !state.userMenu };
    case "SET_SIDEMENU":
      return { ...state, sideMenu: !state.sideMenu };
    case "TOGGLE_PHOTOMODAL":
      return { ...state, photoModal: !state.photoModal };
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
    default:
      return state;
  }
};

export default reducer;
