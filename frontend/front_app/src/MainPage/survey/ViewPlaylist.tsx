import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface ViewPlaylistsProps {
    username: string;
}

const ViewPlaylists: React.FC<ViewPlaylistsProps> = ({ username }) => {
    interface Playlist {
        id: string;
        name: string;
    }

    const [playlists, setPlaylists] = useState<Playlist[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchPlaylists = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/playlists/user/${username}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setPlaylists(response.data);
                setLoading(false);
            } catch (err) {
                setError('Failed to fetch playlists');
                setLoading(false);
            }
        };

        fetchPlaylists();
    }, [username]);

    if (loading) return (<p>Loading...</p>);
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h2>{username}'s Playlists</h2>
            <ul>
                {playlists.map(playlist => (
                    <li key={playlist.id}>{playlist.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default ViewPlaylists;
