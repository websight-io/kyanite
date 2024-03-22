# requires
- `node` 16 https://github.com/nvm-sh/nvm#intro

# how it works
- `npx` is used to run https://www.npmjs.com/package/http-server
- the `http-server` opens the BacktopJS report on http://127.0.0.1:8087/html_report/ (note use of the port `8087`)

# how to run it
## run from terminal
### general
```
sh ./open-report.sh
```

### MacOS
Double click the file `open-report.sh` or drag and drop the `open-report.sh` file into `Terminal` app in your Dock.

# example
## example report
![report](./readme/report.png)

# troubleshooting
## Tests are wrong!
**Problem**: You see something highlighted as wrong while it is a desired change.

**Fix**: Update baselines (image references). 
Run `npm run test:visual:approve` and commit to the repo.

## MacOS Downloads directory permissions
**Problem**: Some MacOS configurations do not allow running that from the user's `Downloads` directory and show "permissions" error while accessing html report file.

**Fix**: before opening report move it to other directory.

## Terminal crashes or shows `EADDRINUSE`
**Problem**: Terminal window closes immediately or shows `EADDRINUSE` error.

**Fix**: Close your all terminal windows - there is probably somewhere another report opened.

**Fix**: There is also possibility that you are running something on port 8087 - close all apps using this port and retry.
If nothing helps try editing `open-report.sh` and change `PORT=8087` to something above `8000` that works for you.
This means instead of going to http://127.0.0.1:8087/html_report/ you will have to go to i.e. http://127.0.0.1:8000/html_report/

**Fix**: Are you sure you have Node installed as described on top of this document? What is output of `node -v`? It should be: `v16.19.0`
