using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        this.WindowState = WindowState.Maximized;
    }

    private void OnLoginButtonClick(object? sender, RoutedEventArgs e)
    {
        string kellnerName = UsernameTextBox.Text;

        var reservierungenPage = new Reservierungen(kellnerName);
        
        // Content auf die zweite Seite (Reservierungen) setzen
        this.Content = reservierungenPage;
    }
}