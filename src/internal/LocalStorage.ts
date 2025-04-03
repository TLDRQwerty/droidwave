const setLocalStorageItem = (key: string, value: string): void => {
  NativeModules.NativeLocalStorageModule.setStorageItem(key, value);
};

const getLocalStorageItem = (key: string): string | null => {
  return NativeModules.NativeLocalStorageModule.getStorageItem(key);
};

const clearLocalStorage = (): void => {
  NativeModules.NativeLocalStorageModule.clearStorage();
};

export { setLocalStorageItem, getLocalStorageItem, clearLocalStorage };
