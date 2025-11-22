@echo off
echo Bringing Nexus app to front...
powershell -Command "Add-Type -TypeDefinition 'using System; using System.Runtime.InteropServices; public class Win32 { [DllImport(\"user32.dll\")] public static extern bool SetForegroundWindow(IntPtr hWnd); [DllImport(\"user32.dll\")] public static extern IntPtr FindWindow(string lpClassName, string lpWindowName); }'; $hwnd = [Win32]::FindWindow($null, 'Nexus - Student Productivity App'); if($hwnd -ne [IntPtr]::Zero) { [Win32]::SetForegroundWindow($hwnd); Write-Host 'App brought to front!'; } else { Write-Host 'App window not found. Try looking in taskbar.'; }"
pause









